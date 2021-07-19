package com.buit.utill.pinyin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author 老花生
 */
public class PinYinHelper {
	private HashMap<String, ArrayList<String>> hypy = new HashMap<String, ArrayList<String>>();
	private static volatile PinYinHelper instance;
	private static final Object LOCK = new Object();

	public PinYinHelper() {
	}

	public static synchronized PinYinHelper getInstance() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					PinYinHelper tmp = new PinYinHelper();
					tmp.loadPinYin();
					instance = tmp;
				}
			}
		}

		return instance;
	}

	public String getFullPinYin(String sentence) {
		char[] charset = sentence.toCharArray();
		int length = charset.length;
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; ++i) {
			char c = charset[i];
			if (this.hypy.get(String.valueOf(c)) != null) {
				ArrayList<String> list = this.hypy.get(String.valueOf(c));
				sb.append(list.get(0));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public String getSimplePinYin(String sentence) {
		if (sentence != null) {
			char[] charset = sentence.toCharArray();
			int length = charset.length;
			StringBuilder sb = new StringBuilder(length);

			for (int i = 0; i < length; ++i) {
				char c = charset[i];
				if (this.hypy.get(String.valueOf(c)) != null) {
					ArrayList<String> list = this.hypy.get(String.valueOf(c));
					String s = (String) list.get(0);
					if (s != null && s.length() > 0 && !isLetter(s.charAt(0))){
						continue;
					}
					sb.append(s.charAt(0));
				} else {
					if (!isLetter(c)){
						continue;
					}
					sb.append(c);
				}
				if (sb.length() == 8) {
					break;
				}
			}
			return sb.toString().toUpperCase();
		} else {
			return "";
		}
	}

	public List<String> traslatePinYins(String sentence) {
		List<String> result = new ArrayList<String>();
		char[] charset = sentence.toCharArray();
		if (charset.length <= 0) {
			return result;
		} else {
			List<String> pinyins = (List<String>) this.hypy.get(String.valueOf(charset[0]));
			String str;
			if (pinyins == null) {
				str = this.getSimplePinYin(sentence);
				result.add(str);
				return result;
			} else {
				Iterator<String> it = pinyins.iterator();

				while (it.hasNext()) {
					String item = (String) it.next();
					result.add(item.substring(0, 1));
				}

				if (charset.length > 1) {
					str = this.getSimplePinYin(sentence.substring(1));

					for (int i = 0; i < result.size(); ++i) {
						result.set(i, result.get(i) + str);
					}
				}

				return result;
			}
		}
	}
	
	public boolean isLetter(char ch){
		return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
	}

	public void loadPinYin() {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("pinyin.txt");
			InputStreamReader rdr = new InputStreamReader(in, "utf-8");
			BufferedReader brdr = new BufferedReader(rdr);

			while (true) {
				String[] items;
				int length;
				do {
					String s;
					if ((s = brdr.readLine()) == null) {
						brdr.close();
						rdr.close();
						return;
					}

					items = s.split(":|,");
					length = items.length;
				} while (length <= 1);

				ArrayList<String> list = new ArrayList<String>(length - 1);

				for (int i = 1; i < length; ++i) {
					String value = items[i].trim();
					if (value.compareTo("") != 0) {
						list.add(value);
					}
				}

				this.hypy.put(items[0].trim(), list);
			}
		} catch (Exception var10) {
			throw new RuntimeException("读取拼音数据字典文件失败", var10);
		}
	}
}
