package block3.MagicSpell;

/*
MagicSpell

  You are given a string spell containing an ancient magic spell. The spell is encrypted, but the cypher is quite simple. To decrypt the spell, you need to find all occurrences of the letters 'A' and 'Z', and then reverse their order. For example, if the encrypted spell is "AABZCADZA", you would first find all the 'A's and 'Z's: "AA_Z_A_ZA". You would then reverse their order: "AZ_A_Z_AA". The final decrypted spell is "AZBACZDAA". Return the decrypted version of the given spell.
 */
public class MagicSpell {

  public String fixTheSpell(String spell) {
    int left = 0;
    int right = spell.length() - 1;

    StringBuilder stringBuilder = new StringBuilder(spell);

    while (left <= right) {
      char charLeft = stringBuilder.charAt(left);
      if (!isAOrZ(charLeft)) {
        left++;
        continue;
      }

      char charRight = stringBuilder.charAt(right);
      if (!isAOrZ(charRight)) {
        right--;
        continue;
      }

      stringBuilder.setCharAt(left, charRight);
      stringBuilder.setCharAt(right, charLeft);

      left++;
      right--;
    }

    return stringBuilder.toString();
  }

  private boolean isAOrZ(char c) {
    return c == 'A' || c == 'Z';
  }

  public static void main(String[] args) {
    MagicSpell magicSpell = new MagicSpell();
    System.out.println(magicSpell.fixTheSpell("AZBASGHNAZAHBNVZZGGGAGGZAZ"));
  }

}
