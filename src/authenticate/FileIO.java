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
		ObjectOutputStream out = null;
		out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(userObj)));
		
		out.writeObject(userList);
		
		out.close();
	}
	
	public synchronized static boolean addUser(User user) throws IOException {
		Iterator<User> it = userList.iterator();
		while(it.hasNext()) {
			if(((User) it.next()).id.equalsIgnoreCase(user.id)) {
				System.out.println(Thread.currentThread().getName() + " 회원가입 실패");
				return false;
			}
		}
		userList.add(user);
		
		System.out.println(Thread.currentThread().getName() + " 회원가입 성공");
		return true;
	}
	
	public static User login(String id, String pw) throws IOException {
		Iterator<User> it = userList.iterator();
		User line;
		while(it.hasNext()) {
			line = (User) it.next();
			if(line.id.equalsIgnoreCase(id) && line.pw.equalsIgnoreCase(pw)) {
				System.out.println(Thread.currentThread().getName() + " 로그인 성공");
				return line;
			}
		}
		System.out.println(Thread.currentThread().getName() + " 로그인 실패");
		return null;
	}
	
	public synchronized static void logout(User user) throws IOException {
		updateUser(user);
		
		System.out.println(Thread.currentThread().getName() + " 로그아웃 성공");
	}
}
