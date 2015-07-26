package net.laochu.design.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class HttpUtils {
	private final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	@Value("#{configProperties['serviceIP']}")  
	private String serviceIP;  
	
	public  String sendHttpPost(String functionName){
		return sendHttpPost(functionName, "");
	}
	public  String sendHttpPost(String functionName,String jsonString){
			String resultString="";
			CloseableHttpClient httpclient = HttpClients.createDefault();  
		 	HttpPost httppost = new HttpPost(serviceIP+functionName);  
	        StringEntity  uefEntity;  
	        try {  
	            uefEntity = new StringEntity(jsonString,"UTF-8");  
	            httppost.setEntity(uefEntity);
	            logger.info("executing request " + httppost.getURI());  
	            logger.info("request params "+jsonString);
	            CloseableHttpResponse response = httpclient.execute(httppost);  
	            try {  
	                HttpEntity entity = response.getEntity();  
	                if (entity != null) {  
	                    resultString=EntityUtils.toString(entity, "UTF-8");
	                }  
	            } finally {  
	                response.close();  
	            }  
	        } catch (ClientProtocolException e) {  
	        	logger.error(e.toString());
	        } catch (UnsupportedEncodingException e1) {  
	        	logger.error(e1.toString());  
	        } catch (IOException e) {  
	        	logger.error(e.toString());  
	        } finally {  
	            // 关闭连接,释放资源    
	            try {  
	                httpclient.close();  
	            } catch (IOException e) {  
	            	logger.error(e.toString());  
	            }  
	        }  
	        logger.info("response result:"+resultString);
	        return resultString;
	}
}
