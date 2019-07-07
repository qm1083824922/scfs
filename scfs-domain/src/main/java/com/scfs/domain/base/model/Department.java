package com.scfs.domain.base.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/24.
 */
public class Department implements Comparable<Department> {

	private Integer id;

	private String name;

	private boolean open = false;

	private List<Department> children;

	public Department() {

	}

	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Department> getChildren() {
		return children;
	}

	public void setChildren(List<Department> children) {
		this.children = children;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public int compareTo(Department o) {
		return this.name.compareTo(o.getName());
	}
}
