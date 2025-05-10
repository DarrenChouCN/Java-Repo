package block3.MagicSpell;

/*
MagicSpell

  You are given a string spell containing an ancient magic spell. The spell is encrypted, but the cypher is quite simple. To decrypt the spell, you need to find all occurrences of the letters 'A' and 'Z', and then reverse their order. For example, if the encrypted spell is "AABZCADZA", you would first find all the 'A's and 'Z's: "AA_Z_A_ZA". You would then reverse their order: "AZ_A_Z_AA". The final decrypted spell is "AZBACZDAA". Return the decrypted version of the given spell.

I use the two-pointer technique to solve this problem:

  1. Initialize two pointers, left and right, starting from the beginning and end of the string, respectively.

  2. If the character at left is not 'A' or 'Z', increment left to skip it.

  3. If the character at right is not 'A' or 'Z', decrement right to skip it.

  4. When both left and right point to either 'A' or 'Z', swap the two characters, then move both pointers inward (left++, right--).

  5. Repeat the process until left >= right. Finally, return the modified string.
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
