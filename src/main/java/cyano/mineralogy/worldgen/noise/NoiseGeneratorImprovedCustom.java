package cyano.mineralogy.worldgen.noise;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.minecraft.world.gen.NoiseGenerator;

public class NoiseGeneratorImprovedCustom extends NoiseGenerator {

    private int[] permutations;
    public double xCoord;
    public double yCoord;
    public double zCoord;
    private static final int PERMUTATION_SIZE = 512;
    private static final int PERMUTATION_MASK = 255;
    private static final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
    private static final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
    private static final double[] GRADIENTS = new double[PERMUTATION_SIZE * 3];
    private static final double[] INTERPOLATION = new double[PERMUTATION_SIZE * 3];
    private static final double[] field_152381_e = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D,
        0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
    private static final double[] field_152382_f = new double[] { 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D,
        1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D };
    private static final double[] field_152383_g = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D,
        1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
    private static final double[] field_152384_h = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D,
        0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
    private static final double[] field_152385_i = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D,
        1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };

    public NoiseGeneratorImprovedCustom() {
        this(new Random());
    }

    public final double lerp(double p_76311_1_, double p_76311_3_, double p_76311_5_) {
        return p_76311_3_ + p_76311_1_ * (p_76311_5_ - p_76311_3_);
    }

    public final double func_76309_a(int p_76309_1_, double p_76309_2_, double p_76309_4_) {
        int j = p_76309_1_ & 15;
        return field_152384_h[j] * p_76309_2_ + field_152385_i[j] * p_76309_4_;
    }

    public NoiseGeneratorImprovedCustom(Random p_i45469_1_) {
        this.permutations = new int[PERMUTATION_SIZE];
        this.xCoord = p_i45469_1_.nextDouble() * 256.0D;
        this.yCoord = p_i45469_1_.nextDouble() * 256.0D;
        this.zCoord = p_i45469_1_.nextDouble() * 256.0D;
        int i;

        for (i = 0; i < PERMUTATION_MASK; this.permutations[i] = i++) {
            ;
        }

        for (i = 0; i < PERMUTATION_MASK; ++i) {
            int j = p_i45469_1_.nextInt(PERMUTATION_MASK - i) + i;
            int k = this.permutations[i];
            this.permutations[i] = this.permutations[j];
            this.permutations[j] = k;
            this.permutations[i + PERMUTATION_MASK] = this.permutations[i];
        }

        // Precalculate gradients and interpolation values
        for (i = 0; i < PERMUTATION_SIZE; i++) {
            int index = i * 3;
            GRADIENTS[index] = field_152381_e[i & 15];
            GRADIENTS[index + 1] = field_152382_f[i & 15];
            GRADIENTS[index + 2] = field_152383_g[i & 15];
            INTERPOLATION[index] = field_152384_h[i & 15];
            INTERPOLATION[index + 1] = field_152385_i[i & 15];
            INTERPOLATION[index + 2] = field_152384_h[i & 15];
        }
    }

    public void populateNoiseArray(double[] p_76308_1_, double p_76308_2_, double p_76308_4_, double p_76308_6_,
        int p_76308_8_, int p_76308_9_, int p_76308_10_, double p_76308_11_, double p_76308_13_, double p_76308_15_,
        double p_76308_17_) {
        if (p_76308_9_ == 1) {
            populateNoiseArray1(
                p_76308_1_,
                p_76308_2_,
                p_76308_4_,
                p_76308_6_,
                p_76308_8_,
                p_76308_10_,
                p_76308_11_,
                p_76308_13_,
                p_76308_15_,
                p_76308_17_);
        } else {
            populateNoiseArray3(
                p_76308_1_,
                p_76308_2_,
                p_76308_4_,
                p_76308_6_,
                p_76308_8_,
                p_76308_9_,
                p_76308_10_,
                p_76308_11_,
                p_76308_13_,
                p_76308_15_,
                p_76308_17_);
        }
    }

    private void populateNoiseArray1(double[] p_76308_1_, double p_76308_2_, double p_76308_4_, double p_76308_6_,
        int p_76308_8_, int p_76308_10_, double p_76308_11_, double p_76308_13_, double p_76308_15_,
        double p_76308_17_) {
        int l;
        int i1;
        double d9;
        double d11;
        int l1;
        double d12;
        int i2;
        int j2;
        double d13;
        int k5;
        int j6;
        double d21;
        double d22;
        k5 = 0;
        double d23 = 1.0D / p_76308_17_;

        for (int j1 = 0; j1 < p_76308_8_; ++j1) {
            d9 = p_76308_2_ + (double) j1 * p_76308_11_ + this.xCoord;
            int i6 = (int) d9;

            if (d9 < (double) i6) {
                --i6;
            }

            int k1 = i6 & 255;
            d9 -= i6;
            d11 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);

            for (l1 = 0; l1 < p_76308_10_; ++l1) {
                d12 = p_76308_6_ + (double) l1 * p_76308_15_ + this.zCoord;
                i2 = (int) d12;

                if (d12 < (double) i2) {
                    --i2;
                }

                j2 = i2 & 255;
                d12 -= i2;
                d13 = d12 * d12 * d12 * (d12 * (d12 * 6.0D - 15.0D) + 10.0D);
                l = this.permutations[k1];
                int i4 = this.permutations[l] + j2;
                int j4 = this.permutations[k1 + 1];
                i1 = this.permutations[j4] + j2;
                d21 = this.lerp(d11, INTERPOLATION[l * 3], INTERPOLATION[i1 * 3]);
                d22 = this.lerp(d11, INTERPOLATION[(i4 * 3) + 1], INTERPOLATION[(i1 * 3) + 1]);
                double d24 = this.lerp(d13, d21, d22);
                j6 = k5++;
                p_76308_1_[j6] += d24 * d23;
            }
        }
    }

    private void populateNoiseArray3(double[] p_76308_1_, double p_76308_2_, double p_76308_4_, double p_76308_6_,
        int p_76308_8_, int p_76308_9_, int p_76308_10_, double p_76308_11_, double p_76308_13_, double p_76308_15_,
        double p_76308_17_) {
        int l;
        double d7 = 1.0D / p_76308_17_;
        double i1 = -1;
        double d8 = 0.0D;
        double d9 = 0.0D;
        double d10 = 0.0D;
        double d11 = 0.0D;
        l = 0;

        for (double l1 = 0; l1 < p_76308_8_; ++l1) {
            double d12 = p_76308_2_ + l1 * p_76308_11_ + this.xCoord;
            int i2 = (int) d12;

            if (d12 < (double) i2) {
                --i2;
            }

            double j2 = i2 & 255;
            d12 -= i2;
            double d13 = d12 * d12 * d12 * (d12 * (d12 * 6.0D - 15.0D) + 10.0D);

            for (int k2 = 0; k2 < p_76308_10_; ++k2) {
                double d14 = p_76308_6_ + (double) k2 * p_76308_15_ + this.zCoord;
                int l2 = (int) d14;

                if (d14 < (double) l2) {
                    --l2;
                }

                int i3 = l2 & 255;
                d14 -= l2;
                double d15 = d14 * d14 * d14 * (d14 * (d14 * 6.0D - 15.0D) + 10.0D);

                for (int j3 = 0; j3 < p_76308_9_; ++j3) {
                    double d16 = p_76308_4_ + (double) j3 * p_76308_13_ + this.yCoord;
                    int k3 = (int) d16;

                    if (d16 < (double) k3) {
                        --k3;
                    }

                    int l3 = k3 & 255;
                    d16 -= k3;
                    double d17 = d16 * d16 * d16 * (d16 * (d16 * 6.0D - 15.0D) + 10.0D);

                    if (j3 == 0 || l3 != i1) {
                        i1 = l3;
                        int k4 = this.permutations[(int) j2] + l3;
                        int l4 = this.permutations[k4] + i3;
                        int i5 = this.permutations[k4 + 1] + i3;
                        int j5 = this.permutations[(int) (j2 + 1)] + l3;
                        int k5 = this.permutations[j5] + i3;
                        int l5 = this.permutations[j5 + 1] + i3;
                        d8 = this.lerp(d13, INTERPOLATION[l4 * 3], INTERPOLATION[l5 * 3]);
                        d9 = this.lerp(d13, INTERPOLATION[(i5 * 3) + 1], INTERPOLATION[(l5 * 3) + 1]);
                        d10 = this.lerp(d13, INTERPOLATION[(k4 * 3) + 2], INTERPOLATION[(j5 * 3) + 2]);
                        d11 = this.lerp(d13, INTERPOLATION[(k5 * 3) + 2], INTERPOLATION[(l4 * 3) + 2]);
                    }

                    double d18 = this.lerp(d17, d8, d9);
                    double d19 = this.lerp(d17, d10, d11);
                    double d20 = this.lerp(d15, d18, d19);
                    int j6 = l++;
                    p_76308_1_[j6] += d20 * d7;
                }
            }
        }
    }

    public void parallelPopulateNoiseArray(double[] p_76308_1_, double p_76308_2_, double p_76308_4_, double p_76308_6_,
        int p_76308_8_, int p_76308_9_, int p_76308_10_, double p_76308_11_, double p_76308_13_, double p_76308_15_,
        double p_76308_17_) {
        ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime()
                .availableProcessors());

        for (int i = 0; i < p_76308_8_; i++) {
            final int j1 = i;
            executor.execute(() -> {
                double[] subArray = new double[p_76308_10_ * p_76308_9_];
                if (p_76308_9_ == 1) {
                    populateNoiseArray1(
                        subArray,
                        p_76308_2_,
                        p_76308_4_,
                        p_76308_6_,
                        1,
                        p_76308_10_,
                        p_76308_11_,
                        p_76308_13_,
                        p_76308_15_,
                        p_76308_17_);
                } else {
                    populateNoiseArray3(
                        subArray,
                        p_76308_2_,
                        p_76308_4_,
                        p_76308_6_,
                        1,
                        p_76308_9_,
                        p_76308_10_,
                        p_76308_11_,
                        p_76308_13_,
                        p_76308_15_,
                        p_76308_17_);
                }
                for (int k = 0; k < subArray.length; k++) {
                    p_76308_1_[j1 * subArray.length + k] += subArray[k];
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
