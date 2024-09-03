package com.seizou.kojo.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto {

	private String        facCd;		   // 工場CD
	private String        affilicateId;    // 所属ID
	private String        affilicateName;  // 所属名
	private String        userId;		   // ユーザーID
	private String        userName;	       // ユーザー名
	private Integer       count;           // 所属人数
	private String        authDiv;	       // 権限区分
	private String        watchAuthFlg;	   // 参照権限フラグ
	private String        oprAuthFlg;	   // 操作権限フラグ
	private String        expireDateFrom;  // 適用日（FROM）
	private String        expireDateTo;	   // 適用日（TO）
	private String        pass;			   // パスワード
}
