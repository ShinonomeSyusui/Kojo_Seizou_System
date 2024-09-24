package com.seizou.kojo.domain.form;

import lombok.Data;

@Data
public class Page {

	
	public static final int OUTPUT_NUM = 5;	// 一度に表示する件数
	private long totalNum;						// 全件数
	private int currentPage = 1;				//現在のページ
	private long maxPage;						//全レコードの件数から算出されるページ数（recordNum/outputNum）
	
	/**
	 * 
	 * @param recordNum
	 */
	public void config(Long recordNum) {
		
		if (recordNum % OUTPUT_NUM != 0 || recordNum == 0) {
			++maxPage;
		}
	}
}
