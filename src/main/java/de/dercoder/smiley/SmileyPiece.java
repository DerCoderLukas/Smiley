package de.dercoder.smiley;

public final class SmileyPiece {
  private String textureValue;
  private String textureSignature;

  public SmileyPiece() {

  }

  public SmileyPiece(
    String textureValue, String textureSignature
  ) {
    this.textureValue = textureValue;
    this.textureSignature = textureSignature;
  }

  public void setTextureValue(String textureValue) {
    this.textureValue = textureValue;
  }

  public String getTextureValue() {
    return textureValue;
  }

  public void setTextureSignature(String textureSignature) {
    this.textureSignature = textureSignature;
  }

  public String getTextureSignature() {
    return textureSignature;
  }
}
