/**
 * Copyright 2014 Red Hat, Inc.
 * 
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 */
package org.fusesource.camel.component.sap;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the component that manages {@link SapTransactionalIDocDestinationEndpoint}.
 * 
 * @author William Collins <punkhornsw@gmail.com>
 * 
 */
public class SapQueuedIDocDestinationComponent extends UriEndpointComponent {

	private static final Logger LOG = LoggerFactory.getLogger(SapQueuedIDocDestinationComponent.class);

	public SapQueuedIDocDestinationComponent() {
		super(SapQueuedIDocDestinationEndpoint.class);
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		if (!uri.startsWith("sap-qidoc-destination:")) { 
			throw new IllegalArgumentException("The URI '" +  uri + "' has invalid scheme; should be 'sap-qidoc-destination:'");			
		}
		// Parse URI
		String[] uriComponents = remaining.split(":");

		if (uriComponents.length < 3) {
			throw new IllegalArgumentException("URI must be of the form: sap-qidoc-destination:<destinationName>:<queueName>:<idocType>[<idocTypeExtension>[<systemRelease>[<applicationRelease>]]]");
		}

		// Extract URI components
		parameters.put("destinationName", uriComponents[0]);
		parameters.put("queueName", uriComponents[1]);
		parameters.put("idocType", uriComponents[2]);
		if(uriComponents.length > 3) {
			parameters.put("idocTypeExtension", uriComponents[3]);
		}
		if(uriComponents.length > 4) {
			parameters.put("systemRelease", uriComponents[4]);
		}
		if(uriComponents.length > 5) {
			parameters.put("applicationRelease", uriComponents[5]);
		}
		SapQueuedIDocDestinationEndpoint endpoint = new SapQueuedIDocDestinationEndpoint(uri, this);

		// Configure Endpoint
		setProperties(endpoint, parameters);
		LOG.debug("Created endpoint '" + uri + "'");

		// Create a document to ensure that the data layer's package registry is
		// loaded with the schema of this endpoint's IDoc type.
		endpoint.createDocument();
		
		return endpoint;
	}

	@Override
    protected void doStart() throws Exception {
    	super.doStart();
    	LOG.debug("STARTED");
    }
    
    @Override
    protected void doStop() throws Exception {
    	super.doStop();
    	LOG.debug("STOPPED");
    }
}
