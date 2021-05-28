package com.hoya.core.page;

import lombok.Data;

import java.util.List;

@Data
public class PageResult {
	/**
	 * 当前页码
	 */
	private int page;

	/**
	 * 每页数量
	 */
	private int rows;

	/**
	 * 记录总数
	 */
	private long totalSize;

	/**
	 * 页码总数
	 */
	private int totalPages;

	/**
	 * 分页数据
	 */
	private List<?> content;

}
