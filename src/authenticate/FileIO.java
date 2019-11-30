package authenticate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileIO {

	private static final File userObj = new File("./user.obj");
	
	public synchronized static void updateUser(User user) throws IOException {
		ObjectOutputStream 	out = null;
		ObjectInputStream 	in = null;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(userObj)));
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(userObj)));
			
			User line;
			try {
				while((line = (User) in.readObject()) != null) {
					if(line.id == user.id) {
						line = user;
					}
					out.writeObject(line);
				}
				out.flush();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} finally {
			if (in != null) in.close();
			if (out != null) out.close();
		}
	}
	
	public synchronized static boolean addUser(User user) throws IOException {
		ObjectOutputStream 	out = null;
		ObjectInputStream 	in = null;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(userObj, true)));
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(userObj)));
			
			User line;
			try {
				while((line = (User) in.readObject()) != null) {
					if(line.id == user.id) {
						return false;
					}
				}
				out.writeObject(user);
				out.flush();
				return true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} finally {
			if (in != null) in.close();
			if (out != null) out.close();
		}
		return false;
	}
	
	public synchronized static User login(String id, String pw) throws IOException {
		ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(userObj)));User line;
				while((line = (User) in.readObject()) != null) {
					if(line.id == id && line.pw == pw) {
						return line;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (in != null) in.close();
			}
			return null;
	}
	
	public synchronized static void logout(User user) throws IOException {
		updateUser(user);
	}
}
