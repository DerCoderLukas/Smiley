package de.dercoder.smiley;

import java.util.List;

public final class SmileyConfiguration {
  private List<SmileyModel> smileys;

  public SmileyConfiguration() {

  }

  public SmileyConfiguration(List<SmileyModel> smileys) {
    this.smileys = smileys;
  }

  public void setSmileys(List<SmileyModel> smileys) {
    this.smileys = smileys;
  }

  public List<SmileyModel> getSmileys() {
    return smileys;
  }
}
