package com.seizou.kojo.domain.dao;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー情報 Dao
 * @author K.Sakuma
 */
@Data
@NoArgsConstructor
public class InsertUserInfoDao {

	private String facCd;				// 工場CD
	private String affilicateId;		// 所属ID
	private String affilicateName;		// 所属名
	private String userId;				// ユーザーID
	private String userName;			// ユーザー名
	private String pass;				// パスワード
	private String rePass;				// パスワード再確認
	private String hashPass;			// パスワード（暗号化）
	private Date expireDateFrom;		// 適用日（FROM）
	private Date expireDateTo;			// 適用日（TO）
	private String authDiv;				// 権限区分
	private boolean watchAuthFlg;		// 参照権限フラグ
	private boolean oprAuthFlg;			// 操作権限フラグ
	private String divCd;				// 区分値
	private Date systemDate;			// システム日時
	private String loginUserId;			// ユーザーID （ログインユーザー）
}
