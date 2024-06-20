package nl.inholland.BankAPI.Model.DTO;

public record LoginResponseDTO(Long userId, String name, String usertype, String token) {
    public String getToken() {
        return this.token;
    }
}
