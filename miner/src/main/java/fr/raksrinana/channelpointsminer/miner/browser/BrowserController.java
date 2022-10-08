package fr.raksrinana.channelpointsminer.miner.browser;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.core.type.TypeReference;
import fr.raksrinana.channelpointsminer.miner.api.passport.exceptions.LoginException;
import fr.raksrinana.channelpointsminer.miner.util.CommonUtils;
import fr.raksrinana.channelpointsminer.miner.util.json.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Cookie;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class BrowserController{
	@NotNull
	private final SelenideDriver driver;
	
	public void ensureLoggedIn() throws LoginException{
		openTurboPage();
		
		if(!isLoggedIn()){
			throw new LoginException("Not logged in");
		}
	}
	
	private void openTurboPage(){
		driver.open("https://www.twitch.tv/turbo");
		
		var acceptCookiesButton = driver.$("button[data-a-target=consent-banner-accept]");
		if(acceptCookiesButton.is(Condition.visible)){
			acceptCookiesButton.click();
		}
	}
	
	public void login() throws LoginException, IOException{
		log.info("Logging in");
		openTurboPage();
		
		if(isLoggedIn()){
			log.info("Already logged in");
			return;
		}
		
		var manager = driver.getWebDriver().manage();
		
		JacksonUtils.read(askUserLogin(), new TypeReference<List<CookieData>>(){})
				.stream()
				.map(c -> new Cookie(c.getName(), c.getValue(), c.getDomain(), c.getPath(), c.getExpiry(), c.isSecure(), c.isHttpOnly(), c.getSameSite()))
				.forEach(manager::addCookie);
		
		driver.refresh();
		if(!isLoggedIn()){
			throw new LoginException("Not logged in");
		}
	}
	
	@NotNull
	private String askUserLogin(){
		log.error("Not logged in, please input cookies");
		return CommonUtils.getUserInput("Provide your session cookies under JSON format (you can use an extension like Cookie-Editor): ");
	}
	
	private boolean isLoggedIn(){
		var loginButton = getLoginButton();
		return !loginButton.is(Condition.visible);
	}
	
	@NotNull
	private SelenideElement getLoginButton(){
		return driver.$("button[data-a-target=login-button]");
	}
}
