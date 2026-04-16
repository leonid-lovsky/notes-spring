import jakarta.validation.constraints.NotNull;

import java.util.UUID;

record UserResponse(
    @NotNull UUID id,
    @NotNull String username
) {

}
