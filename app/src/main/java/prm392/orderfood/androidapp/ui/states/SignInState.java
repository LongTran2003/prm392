package prm392.orderfood.androidapp.ui.states;

import prm392.orderfood.domain.models.auth.Token;

public abstract class SignInState {

    private SignInState() {}
    public static final class Idle extends SignInState {}
    public static final class Loading extends SignInState {}
    public static final class Success extends SignInState {
        private final Token token;
        public Success(Token token) { this.token = token; }
        public Token getToken() { return token; }
    }
    public static final class Error extends SignInState {
        private final String errorMessage;
        public Error(String errorMessage) { this.errorMessage = errorMessage; }
        public String getErrorMessage() { return errorMessage; }
    }
}
