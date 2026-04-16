import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NoteResponse(
    @NotNull UUID id,
    @NotNull String content
) {

}
