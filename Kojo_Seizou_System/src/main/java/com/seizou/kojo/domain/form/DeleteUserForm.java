package com.seizou.kojo.domain.form;


import java.util.List;

import lombok.Data;

/**
 * ユーザー情報削除のFormクラス
 * @author K.Tomonari
 */
@Data
public class DeleteUserForm {
	
	private List<String>   deleteUser;   // 削除ユーザー
	private List<String>   facCd;		// 工場CD
	private List<String>   affilicateId;   // 所属ID
	private List<String>   userId;		 // ユーザーID
}