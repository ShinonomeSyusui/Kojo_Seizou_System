package com.seizou.kojo.domain.dao;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー情報一覧 Dao
 * @author K.Tomonari
 */
@Data
@NoArgsConstructor
public class UserInfoDao {

	private String   facCd;		      // 工場CD
	private String   affilicateId;    // 所属ID
	private String   affilicateName;  // 所属名
	private String   userId;		  // ユーザーID
	private String   userName;	      // ユーザー名
	private List<String> authDiv;	      // 権限区分
	private Boolean     watchAuthFlg;	  // 参照権限フラグ
	private Boolean     oprAuthFlg;	  // 操作権限フラグ
	private String   expireDateFrom;    // 適用日（FROM）
	private String   expireDateTo;	  // 適用日（TO）
	private String   pass;	          // パスワード
	
}

