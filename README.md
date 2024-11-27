## Least Square Method
#### by AJI Hsu 2024/11/26 ~ 2024/11/27
---
* static functionSet cannot create any object due to the private Constructer
* the only method visible by Main is `double[] run(double[] x1, double[] x2, double[] y)`, which is public and can be called from outside.
* the return is value `x = { a, b, c };` where `y = ax1 + bx2 + c`

### Test Environment
* The playground is open to the public and can be edited by anyone, so if you want to test the code, I will open the playground for you to test. [LINK](https://leetcode.com/playground/krTvtUT2)
* All you can edit is the array x1, x2, y, and they MUST have the same length.
![image](https://github.com/user-attachments/assets/d417ccc8-9faf-4279-ae2e-3aec721c03ad)

### JS version:
```javascript=
// Test environment
const main = () => {
    // Test values for x1, x2, and y
    const x1 = [1, 3, 4, 5, 24, 203, 23920];
    const x2 = [24, 245, 2513, 3333, 9045, 10293, 105000];
    const y = [90, 209, 329, 4444, 5789, 7890, 9013];

    // Running the function to find the curve coefficients
    const result = FunctionSet.run(x1, x2, y);

    // Output the result
    console.log(result);
};

class FunctionSet {
    static doubleError = 0.001;

    static run(x1, x2, y) {
        return this.find2VariableCurve(x1, x2, y);
    }

    // Transpose the matrix
    static transpose(arr) {
        const m = arr.length;
        const n = arr[0].length;
        const trans = Array(n).fill().map(() => Array(m));

        for (let i = 0; i < m; i++) {
            for (let j = 0; j < n; j++) {
                trans[j][i] = arr[i][j];
            }
        }
        return trans;
    }

    // Matrix multiplication
    static multiply(arr1, arr2) {
        const m1 = arr1.length;
        const n1 = arr1[0].length;
        const m2 = arr2.length;
        const n2 = arr2[0].length;

        if (n1 !== m2) {
            console.error("Error: n1 != m2 at function : multiply");
            return null;
        }

        const result = Array(m1).fill().map(() => Array(n2).fill(0));

        for (let i = 0; i < m1; i++) {
            for (let j = 0; j < n2; j++) {
                for (let k = 0; k < m2; k++) {
                    result[i][j] += arr1[i][k] * arr2[k][j];
                }
            }
        }

        return result;
    }

    // Check if vector is a zero vector
    static isZeroVector(v) {
        return v.every(val => val <= this.doubleError);
    }

    // Gram-Schmidt Process to orthonormalize vectors
    static GramSchmidtProcess(A) {
        const m = A.length; // Row number
        const n = A[0].length; // Rn
        let idx = 0;
        const Q = [];

        let v;
        let len;

        // First step
        v = [...A[idx++]]; // Error vector (let e1 = v1)
        len = 0;
        for (let i of v) {
            len += i * i;
        }

        len = Math.sqrt(len);
        for (let i = 0; i < n; i++) { // Normalization
            v[i] /= len;
        }

        // Step iteration
        while (!this.isZeroVector(v)) {
            Q.push(v); // Add to Q

            if (idx >= m) break;

            // Find error vector
            v = [...A[idx++]];
            for (let q of Q) { // v_k = v_k - sigma(k-1, 1)_qi^T * v_k * qi
                let t = 0;
                for (let i = 0; i < q.length; i++) {
                    t += q[i] * v[i];
                }
                for (let i = 0; i < v.length; i++) {
                    v[i] -= t * q[i];
                }
            }

            // Normalization
            len = 0;
            for (let i of v) {
                len += i * i;
            }

            len = Math.sqrt(len);
            for (let i = 0; i < n; i++) { // Normalization
                v[i] /= len;
            }
        }

        return Q;
    }

    // QR Factorization
    static QRFactorization(A) {
        const as = [];
        for (let i = 0; i < A[0].length; i++) {
            const t = Array(A.length).fill(0);
            for (let j = 0; j < A.length; j++) {
                t[j] = A[j][i];
            }
            as.push(t);
        }

        const qs = this.GramSchmidtProcess(as); // Orthonormal column vectors

        const m = qs[0].length; // Dim of column vectors
        const k = qs.length; // Number of orthonormal vectors

        // Q
        const Q = Array(m).fill().map(() => Array(k).fill(0));
        for (let i = 0; i < k; i++) {
            for (let j = 0; j < m; j++) {
                Q[j][i] = qs[i][j];
            }
        }

        // R
        const R = Array(k).fill().map(() => Array(k).fill(0));
        for (let i = 0; i < k; i++) {
            for (let j = i; j < k; j++) {
                for (let t = 0; t < m; t++) {
                    R[i][j] += as[j][t] * qs[i][t];
                }
            }
        }

        return [Q, R];
    }

    // Solve the system of linear equations for x
    static findX(A, b) {
        const QR = this.QRFactorization(A);
        const Q = QR[0]; // m x k
        const R = QR[1]; // k x k
        const k = QR[0][0].length;

        const c = this.multiply(this.multiply(this.transpose(R), this.transpose(Q)), b); // k x 1
        const y = Array(k).fill().map(() => [0]);

        const RT = this.transpose(R);
        for (let i = 0; i < k; i++) {
            let temp = 0;
            for (let j = 0; j < i; j++) {
                temp += RT[i][j] * y[j][0];
            }
            y[i][0] = (c[i][0] - temp) / RT[i][i];
        }

        const x = Array(k).fill().map(() => [0]);
        for (let i = k - 1; i >= 0; i--) {
            let temp = 0;
            for (let j = k - 1; j > i; j--) {
                temp += R[i][j] * x[j][0];
            }
            x[i][0] = (y[i][0] - temp) / R[i][i];
        }

        return x.map(val => val[0]);
    }

    // Find coefficients for a 2-variable linear equation
    static find2VariableCurve(x1, x2, y) {
        const A = x1.map((_, i) => [x1[i], x2[i], 1]);
        const b = y.map(val => [val]);

        return this.findX(A, b);
    }
}

// Call the main function to execute
main();

```
