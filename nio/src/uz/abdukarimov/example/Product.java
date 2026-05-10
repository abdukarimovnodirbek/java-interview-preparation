package uz.abdukarimov.example;

// ── Model ─────────────────────────────────────────────────────
@JsonSerializable
public record Product(
        @JsonField(name = "product_name", required = true)
        String name,

        @JsonField(required = true)
        double price,

        @JsonField(ignore = true)   // serializatsiya qilinmaydi
        String internalCode,

        int stock
) {
}