package com.girnarsoft.employees;

import java.sql.Connection;
/**
 * shows all the funtions of ceo and  hr show that we can create only service class object and don't need to 
 * create separate objects for hr and ceo
 * @author gspl
 *
 */
interface Services {
	public int addEmployee(Connection conn, String empDesignation);

	public void promote(Connection conn);

	public void deleteEmployee(Connection conn, int empId);

	public void changeSupervisor(Connection conn);

	// public void showEmployeesUnderMe(Map<Integer, Employee> employees, int
	// empId);
}