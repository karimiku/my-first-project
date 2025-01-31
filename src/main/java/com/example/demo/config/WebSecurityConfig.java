package com.example.demo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.constant.UrlConst;

import lombok.RequiredArgsConstructor;
/** 
 * SpringSecurityの設定を行うクラス
 * SpringSecurityの有効化と、認証設定を管理する
 */

@EnableWebSecurity //SpringSecurityを有効化
@Configuration //Springの設定クラス
@RequiredArgsConstructor //finalのフィールドをコンストラクタ自動注入
public class WebSecurityConfig {
	
    private final PasswordEncoder passwordEncoder;
	
	//ユーザー情報取得Service
	private final UserDetailsService userDetailsService;
	
	//メッセージ取得
	private final MessageSource messageSource;
	
	//ユーザー名のname属性
	private final String USERNAME_PARAMETER = "loginId";
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.csrf(csrf -> csrf.disable())
	    .authorizeHttpRequests(
	        authorize -> authorize.requestMatchers(UrlConst.LOGIN, UrlConst.SIGNUP
	        		,"/css/**", "/javascript/**").permitAll()
	        .requestMatchers(UrlConst.TASK_REGISTER, UrlConst.MENU,"/api/**")
	        .authenticated() // 認証が必要
	        .anyRequest()
	        .authenticated()
	    )
	    .formLogin(
				login -> login.loginPage(UrlConst.LOGIN) //自作ログイン画面を使うための設定
				      .usernameParameter(USERNAME_PARAMETER)//ユーザー名パラメータのname
				      .defaultSuccessUrl(UrlConst.MENU))//ログイン成功後のリダイレクトURL
		.logout(logout -> logout.logoutSuccessUrl(UrlConst.LOGIN));//ログアウト後のリダイレクトURL
		
		return http.build();
	}
	
	@Bean
	AuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		provider.setMessageSource(messageSource);
		return provider;
	}
	


}
