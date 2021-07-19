package com.buit.utill;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Test {
	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {

		String str="待加密";

		
//		String key = "AD42F6697B035B7580E4FEF93BE20BAD";
//		String miwen = AESUtil.encrypt(str, key, 2);
//		String jiemi = AESUtil.decrypt(str, key, 2);
//		System.out.println("密文=="+miwen+"-----解密后:"+jiemi);

		
//		String ret="{\"binding_Relation_response\":{\"result\":0,\"prtms\":\"15821698254\",\"unitID\":\"10000000003\",\"smbms\":\"95013597658604\",\"otherms\":\"\"}}";
//		System.out.println(ret); 
//		ObjectMapper objectMapper = new ObjectMapper();
//		TZBindingResponse tz=objectMapper.readValue(ret, TZBindingResponse.class);
//		System.out.println(tz.getSuccessResult().getSmbms());
		
//		String key = "AD42F6697B035B7580E4FEF93BE20BAD";
		
		
//		String content = "test中文";
//		//随机生成密钥
//		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//		 
//		//构建
//		AES aes = SecureUtil.aes(key);
//		 
//		//加密
//		byte[] encrypt = aes.encrypt(content);
//		//解密
//		byte[] decrypt = aes.decrypt(encrypt);
//		 
//		//加密为16进制表示
//		String encryptHex = aes.encryptHex(content);
//		//解密为原字符串
//		String decryptStr = aes.decryptStr(encryptHex);
		
//		
//		String content = "test中文";
//		 
//		//随机生成密钥
//		System.out.println(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()));
//		Provider Provider ;
//		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//		 
//		//构建
//		SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
//		 
//		//加密
//		byte[] encrypt = aes.encrypt(content);
//		//解密
//		byte[] decrypt = aes.decrypt(encrypt);
//		 
//		//加密为16进制表示
//		String encryptHex = aes.encryptHex(content);
//		//解密为字符串
//		String decryptStr = aes.decryptStr(encryptHex);

		
//		String str="待加密";
//		String miwen=SecureUtil.aes().encryptHex(str);
//		String jiemi=SecureUtil.aes().decryptStr(miwen);
//		System.out.println("密文=="+miwen+"-----解密后:"+jiemi);
//
//		String miwen2=SecureUtil.aes().encryptBase64(str);	
//		String jiemi2=SecureUtil.aes().decryptStr(miwen2);
//		
//		System.out.println("密文=="+miwen2+"-----解密后:"+jiemi2);
//		String key = "AD42F6697B035B7580E4FEF93BE20BAD";
		
//		String str="待加密";
//		String miwen=SecureUtil.aes(Base64.decode(key.getBytes())).encryptHex(str);
//		String jiemi=SecureUtil.aes(Base64.decode(key.getBytes())).decryptStr(miwen);
//		System.out.println("密文=="+miwen+"-----解密后:"+jiemi);
//		String miwen2=SecureUtil.aes(key).encryptBase64(str);	
//		String jiemi2=SecureUtil.aes(key).decryptStr(miwen2);
//		
//		System.out.println("密文=="+miwen2+"-----解密后:"+jiemi2);
		
	}
}
