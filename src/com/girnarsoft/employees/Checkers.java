package com.girnarsoft.employees;

import java.util.Map;
import java.util.Scanner;

/**
 * Checkers class is used for checking 
 * that users enter integer value in empId and String name checker
 * authenticate user through his employeeId and password
 * @author gspl
 *
 */
public class Checkers {
	
	public static final Scanner SC = new Scanner(System.in);
	/*
	 * validate that users enters integer value in int field type 
	 */
	protected String validateInt(String str) {
		if (str.isEmpty()) {
			System.out.println("Wrong Input! try again");
			str = validateInt(SC.nextLine());
		} else {
			for (int index = 0; index < str.length(); index++) {
				// System.out.println(str.charAt(i)+ " ");
				if ((str.charAt(index) >= '1' && str.charAt(index) <= '9') || str.charAt(index) == '0') {
					continue;
				}
				System.out.println("Wrong Input! try again ");
				str = validateInt(SC.nextLine());

			}
		}
		if (str.length() > 6) {
			System.out.println("Integer out of bound error or empId is not in range!");
			str = validateInt(SC.nextLine());
		}
		return str;
	}


	/*
	 * validate Name to be of right format
	 */
	protected String validateString(String name) {
		//regular exper
		String regex = "^[a-zA-Z]+$"; 
		if (!name.matches(regex)) {
			System.out.println("Wrong input ! try again");
			name = validateString(SC.nextLine());
		}
		return name;

	}

}
