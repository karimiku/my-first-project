package com.example.demo.authentication;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー情報を取得し、SpringSecurity用のUserDetailsを作成するクラス。
 * ユーザーのアカウントロック・ロック解除の処理も含む
 */
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	//ユーザー情報を管理するリポジトリ
	private final UserInfoRepository repository;

	//アカウントロックを行うログイン失敗回数の上限値
	@Value("${security.locking-border-count}")
	private int lockingBorderCount;
	
    //アカウントロックの継続時間
	@Value("${securiy.locking-time}")
	private int lockingTime;
	
/**
 * ログインIDからユーザー情報を取得し、SpringSecurityのUserDetailsを生成する。
 * 
 * @param username ログインID
 * @return UserDetails ユーザー情報
 * @throws UsernameNotFoundException
 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var userInfo = repository.findByLoginId(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		
		var accountLockedTime = userInfo.getAccountLockedTime();
		var isAccountLocked = accountLockedTime != null 
				&& accountLockedTime.plusMinutes(lockingTime).isAfter(LocalDateTime.now());
		
		return User.withUsername(userInfo.getLoginId())
				.password(userInfo.getPassword())
				.roles("USER")
				.disabled(userInfo.isDisabled())
				.accountLocked(isAccountLocked).build();
		}
	
	/**
	 * 認証失敗時にログイン失敗回数を加算、ロック日時を更新する
	 */
	@EventListener
	public void handle(AuthenticationFailureBadCredentialsEvent event) {
		var loginId = event.getAuthentication().getName();
		repository.findByLoginId(loginId).ifPresent(userInfo -> {
			repository.save(userInfo.incrementLoginFailureCount());
			var isReachFailureCount = userInfo.getLoginFailureCount() == lockingBorderCount;
			if(isReachFailureCount) {
				repository.save(userInfo.updateAccountLocked());
			}
		});
	}
	
	/**
	 * 認証成功時にログイン失敗回数をリセットする
	 */
	@EventListener
	public void handle(AuthenticationSuccessEvent event) {
		var loginId = event.getAuthentication().getName();
		repository.findByLoginId(loginId).ifPresent(userInfo -> {
			repository.save(userInfo.resetLoginFailureInfo());
		});
	}

}