package com.seizou.kojo.domain.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginationForm {

	//ページネイション
	private int count;              //総ユーザー数
	private int first;              //最初のページ
	private int prev;               //前ページ
	private int next;               //次ページ
	private int last;               //最後のページ
	//private int totalPages;            //総ページ数
	//private int currentPage;         //現在のページ
	private int offset;             //オフセット値
	private int limit;              //リミット値
}
