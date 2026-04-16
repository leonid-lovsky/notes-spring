import jakarta.validation.constraints.NotNull;

record UserRequest(
    @NotNull String username
) {

}
