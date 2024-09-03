package com.seizou.kojo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * セキュリティクラス
 * @author M.Kimura
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

	// パスワードエンコーダーのBean定義
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// データベースへアクセスするためのクラス
	@Autowired
	private DataSource dataSource;
	
	// ユーザーIDとパスワードを取得するSQL文
	private static final String USER_SQL = "SELECT"
		+ " user_id,"
		+ " hash_pass,"
		+ " true"											// 使用可否（Enabled）：true設定
	    + " FROM"
	    + " user_info"
	    + " WHERE"
	    + " user_id = ?";
	
	// ユーザーのROLEを取得するSQL文
	private static final String ROLE_SQL = "SELECT"
		+ " user_id,"
		+ " auth_div"
		+ " FROM"
		+ " user_info"
		+ " WHERE"
		+ " user_id = ?";
	
	/**
	 * WEBセキュリティ
	 * セキュリティ対策が不要なリソースを適用外にする
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/webjars/**")
			.antMatchers("/css/**")
			.antMatchers("/js/**");
	}

	/**
	 * URLセキュリティ
	 * URLごとにセキュリティ設定を変更する
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// ログイン不要ページの設定
		http.authorizeRequests()
			.antMatchers("/webjars/**").permitAll()				// webjarsへアクセス許可
	        .antMatchers("/css/**").permitAll() 				// cssへアクセス許可
	        .antMatchers("/js/**").permitAll()					// jsへアクセス許可
	        .antMatchers("/b-forme_Kojo/pc/001").permitAll() 	// ログインページは直リンクOK
	        .anyRequest().authenticated(); 						// それ以外は直リンク禁止
		
		// ログイン処理
		http.formLogin()
			.loginProcessingUrl("/b-forme_Kojo/pc/002")			// ログイン処理のパス
	        .loginPage("/b-forme_Kojo/pc/001")					// ログインページの指定
	        .failureUrl("/b-forme_Kojo/pc/001")					// ログイン失敗時の遷移先
	        .usernameParameter("userId")						// ログインページのユーザーID
	        .passwordParameter("userPass")						// ログインページのパスワード
	        .defaultSuccessUrl("/b-forme_Kojo/pc/002", true);	// ログイン成功後の遷移先（「true」設定により強制的に遷移する）
		
		// ログアウト処理
		http.logout()
			.logoutUrl("/b-forme_Kojo/pc/001")					// ログアウト処理のパス(POST)
			.logoutSuccessUrl("/b-forme_Kojo/pc/001");			// ログアウト成功後の遷移先
		
	}

	/**
	 * WEBセキュリティ
	 * 認証方法の実装を設定
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		// ログイン処理時のユーザー情報を、DBから取得する
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery(USER_SQL)
			.authoritiesByUsernameQuery(ROLE_SQL)
			.passwordEncoder(passwordEncoder());			//ログイン時のパスワード復号
		
	}
	
//	// 暗号化（ハッシュ化）されたパスワードを得るためだけのmain()メソッド
//	// Springセキュリティの設定には無関係なメソッドです。
//	public static void main(String[] args) {
//		
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		
//		// 「""」内のパスワード暗号化して、コンソールへ出力
//		String password1 = passwordEncoder.encode("admin01");
//		System.out.println(password1);
//		
//	}
	
}