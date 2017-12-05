package com.tariq.muelinaction;

import org.junit.Assert;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.NullPayload;

public class ProductServiceFunctionalTestCase extends FunctionalTestCase{

	@Override
	protected String getConfigResources() {
		return "./src/main/app/product_registration.xml";
	}
	
	@Test
	public void testCanRegisterProducts() throws Exception{
		MuleClient client = muleContext.getClient();
		String productAsJSON = "{\"name\":\"Widget\", \"price\": 9.99,\"weight\": 1.0, \"sku\":\"abcd-12345\"}";
		client.dispatch("http://localhost:8081/products", productAsJSON, null);
		MuleMessage resultMessage = client.request("jms://products", RECEIVE_TIMEOUT);
		
		Assert.assertNotNull(resultMessage);
		Assert.assertNull(resultMessage.getExceptionPayload());
		Assert.assertFalse(resultMessage.getPayload() instanceof NullPayload);
		Assert.assertEquals(productAsJSON, resultMessage.getPayloadAsString());
	}

}
