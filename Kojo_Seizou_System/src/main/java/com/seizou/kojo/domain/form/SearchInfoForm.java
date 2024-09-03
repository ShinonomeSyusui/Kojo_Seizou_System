package com.seizou.kojo.domain.form;


import java.util.List;

import lombok.Data;

/**
 * ユーザー情報検索のFormクラス
 * @author K.Tomonari
 */
@Data
public class SearchInfoForm {
	
	private String   affilicateId;   // 所属ID
	private String   userId;		 // ユーザーID
	private String   userName;	     // ユーザー名
	private List<String> authDiv;        // 権限区分
	private String   expireDateFrom; // 適用日（FROM）
	private String   expireDateTo;   // 適用日（TO）
}