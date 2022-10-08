package fr.raksrinana.channelpointsminer.miner.api.passport;

import fr.raksrinana.channelpointsminer.miner.api.gql.gql.GQLApi;
import fr.raksrinana.channelpointsminer.miner.api.gql.gql.data.GQLResponse;
import fr.raksrinana.channelpointsminer.miner.api.gql.gql.data.reportmenuitem.ReportMenuItemData;
import fr.raksrinana.channelpointsminer.miner.api.gql.gql.data.types.User;
import fr.raksrinana.channelpointsminer.miner.factory.ApiFactory;
import kong.unirest.core.Cookie;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwitchLoginTest{
	private static final String USER_ID = "user-id";
	private static final String USERNAME = "username";
	private static final String ACCESS_TOKEN = "access-token";
	
	@Mock
	private GQLApi gqlApi;
	@Mock
	private GQLResponse<ReportMenuItemData> gqlResponse;
	@Mock
	private ReportMenuItemData reportMenuItemData;
	@Mock
	private User user;
	
	@BeforeEach
	void setUp(){
		lenient().when(gqlApi.reportMenuItem(USERNAME)).thenReturn(Optional.of(gqlResponse));
		lenient().when(gqlResponse.getData()).thenReturn(reportMenuItemData);
		lenient().when(reportMenuItemData.getUser()).thenReturn(user);
		lenient().when(user.getId()).thenReturn(USER_ID);
	}
	
	@Test
	void getUserIdFromCookies(){
		var tested = TwitchLogin.builder()
				.accessToken(ACCESS_TOKEN)
				.username(USERNAME)
				.cookies(List.of(new Cookie("persistent=%s%%3A%%3Aabcdefghijklmnopqrstuvwxyz".formatted(USER_ID))))
				.build();
		
		try(var apiFactory = Mockito.mockStatic(ApiFactory.class)){
			assertThat(tested.fetchUserId(gqlApi)).isEqualTo(USER_ID);
			
			apiFactory.verifyNoInteractions();
		}
	}
	
	@Test
	void getUserIdFromApi(){
		var tested = TwitchLogin.builder()
				.accessToken(ACCESS_TOKEN)
				.username(USERNAME)
				.build();
		
		assertThat(tested.fetchUserId(gqlApi)).isEqualTo(USER_ID);
	}
	
	@Test
	void getUserIdSavesResult(){
		var tested = TwitchLogin.builder()
				.accessToken(ACCESS_TOKEN)
				.username(USERNAME)
				.build();
		
		assertThat(tested.fetchUserId(gqlApi)).isEqualTo(USER_ID);
	}
	
	@Test
	void getUserIdFromApiNoResponse(){
		var tested = TwitchLogin.builder()
				.accessToken(ACCESS_TOKEN)
				.username(USERNAME)
				.build();
		
		when(gqlApi.reportMenuItem(USERNAME)).thenReturn(Optional.empty());
		
		assertThrows(IllegalStateException.class, () -> tested.fetchUserId(gqlApi));
	}
	
	@Test
	void getUserIdAsInt(){
		var tested = TwitchLogin.builder()
				.accessToken(ACCESS_TOKEN)
				.username(USERNAME)
				.userId("123456")
				.build();
		
		assertThat(tested.getUserIdAsInt(gqlApi)).isEqualTo(123456);
	}
}
