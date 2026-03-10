package prm392.orderfood.data.mapper;

import prm392.orderfood.data.datasource.remote.modelResponse.auth.TokenResponse;
import prm392.orderfood.domain.models.auth.Token;

public class TokenMapper {
    public static Token mapToDomain(TokenResponse response) {
        Token token = new Token();
        token.setAccessToken(response.getAccessToken());
        token.setRefreshToken(response.getRefreshToken());
        token.setUserRole(response.getUserRole());
        token.setUserId(response.getUserId());
        return token;
    }
}
