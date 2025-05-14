package quiz;

import java.util.Arrays;

/*
FillTheBoar

  There is a hungry wild boar blocking your access to the forest where a treasure is buried.  A local shepherd has told you a secret:  the wild boar loves eating muffins and when it gets full it falls sleep. 

  Thus, you have made a plan to fill the boar until it falls sleep and you have bought a collection of muffins for that purpose. Their sizes in grams are the elements of the int[] muffins. As long as the wild boar feels hungry, you will select one of the muffins and feed it to the wild boar. Once the wild boar is fed a muffin, it will eat the whole muffin, even if doing so exceeds its stomach capacity - it will somehow stuff all the muffin into its digestive tract.

  Most animals get full when their stomach reach capacity. However, some wild boars do not feel immediately full and keep eating more muffins. The variable delay describes such variability. 
  •	If delay = 0, the boar stops feeling hungry as soon as the total amount of muffin eaten reaches or exceeds its stomach capacity. 
  •	if delay is positive, the boar stops feeling hungry only after  eating delay additional muffins. 

  As you want the boar to sleep for a long time you want to feed him as much quantity as possible.  Return the highest muffin amount that you will feed to the wild boar.
 */
public class FillTheBoar {

  public int eaten(int stomach, int[] muffins, int delay) {
    Arrays.sort(muffins);
    int n = muffins.length;

    int total = 0;
    int restStomach = stomach;
    for (int i = 0; i < n; i++) {
      if (restStomach > muffins[i]) {
        total += muffins[i];
        restStomach -= muffins[i];
        muffins[i] = -1;
      } else {
        break;
      }
    }

    for (int i = muffins.length - 1; i >= 0; i--) {
      if (delay > 0 && muffins[i] != -1) {
        total += muffins[i];
        delay--;
      } else {
        break;
      }
    }

    return total;
  }

  public static void main(String[] args) {
    FillTheBoar fillTheBoar = new FillTheBoar();
    System.out.println(fillTheBoar.eaten(4700, new int[] { 1000, 8000, 2000, 5000, 3000 }, 0));
  }
}
