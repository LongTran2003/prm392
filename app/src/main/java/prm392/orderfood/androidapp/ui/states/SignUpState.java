package prm392.orderfood.androidapp.ui.states;

public abstract class SignUpState {
    private SignUpState() {}

    public static final class Idle extends SignUpState {}

    public static final class Loading extends SignUpState {}

    public static final class Success extends SignUpState {
        private final String message;

        public Success(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static final class Error extends SignUpState {
        private final String errorMessage;

        public Error(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
