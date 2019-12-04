package authenticate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class FileIO {

	private static final File userObj = new File("./user.obj");
	private static List<User> userList = new Vector<>();
	
	public static List<User> getUserList() {
		return userList;
	}
	
	public synchronized static void readUserList() throws IOException {
		ObjectInputStream in = null;
		
		try {
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(userObj)));
			userList = (List<User>) in.readObject();
		} catch (FileNotFoundException e) {
			userObj.createNewFile();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static void updateUser(User user) throws IOException {
		for(int i = 0; i < userList.size(); i++) {
			if((userList.get(i).id).equals(user.id)) {
				userList.set(i, user);
			}
		}
	}
	
	public synchronized static boolean addUser(User user) throws IOException {
		Iterator<User> it = userList.iterator();
		while(it.hasNext()) {
			if(((User) it.next()).id.equalsIgnoreCase(user.id)) return false;
		}
		userList.add(user);
		ObjectOutputStream out = null;
		out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(userObj)));
		
		out.writeObject(userList);
		
		out.close();
		
		System.out.println("회원가입 성공");
		return true;
	}
	
	public static User login(String id, String pw) throws IOException {
		Iterator<User> it = userList.iterator();
		User line;
		while(it.hasNext()) {
			line = (User) it.next();
			if(line.id.equalsIgnoreCase(id) && line.pw.equalsIgnoreCase(pw)) {
				System.out.println("로그인 성공");
				return line;
			}
		}
		return null;
	}
	
	public synchronized static void logout(User user) throws IOException {
		updateUser(user);
	}
}
