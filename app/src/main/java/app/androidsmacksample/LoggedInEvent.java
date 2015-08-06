package app.androidsmacksample;

public class LoggedInEvent {

    private boolean successful;

    public LoggedInEvent(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
