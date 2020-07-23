package webBrowser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class BOOK_MARK {
	
	public static void main(String[] args) {
		 
		new BOOK_MARK();
	}
	
	public String getUrl(String name) {
		FileReader fr = null;
		BufferedReader br = null;
		String url = null;
		File file = new File("D:\\Test\\bookmark\\" + name);
		if (file.exists()) {
			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				url = br.readLine();
				//if (br != null)
					br.close();
				//if (fr != null)
					fr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					//if (br != null)
						br.close();
					//if (fr != null)
						fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	public boolean saveUrl(String name, String url, boolean isCover) {
		File file = new File("D:\\Test\\bookmark\\" + name);
		FileOutputStream out = null;
		try {
			if (!file.exists())
				file.createNewFile();
			else {
				if (isCover) {
					file.delete();
					file.createNewFile();
				}
				return false;
			}
			out = new FileOutputStream(file, true);
			StringBuffer sb = new StringBuffer();
			sb.append(url);
			out.write(sb.toString().getBytes("utf-8"));
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean delUrl(String name) {
		File file = new File("D:\\Test\\bookmark\\" + name);
		if (file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}
	public String[] getFavorite() {
		File[] files = null;
		String[] names;
		File f = new File("D:\\Test\\bookmark");
		if (f.exists()) {
			files = f.listFiles();
		}
		names = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			names[i] = files[i].getName();
		}
		return names;
	}

	
}
