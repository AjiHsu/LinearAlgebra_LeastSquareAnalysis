## Least Square Method
#### by AJI Hsu 2024/11/26 ~ 2024/11/27
---
* FunctionSet cannot create any object due to the private Constructer
* All method is static
* The only method visible by Main is `double[] run(double[] x1, double[] x2, double[] y)`, which is public and can be called from outside.
* The return value is `x = { a, b, c };` where `y = ax1 + bx2 + c`

### Core Linear Algebra Method
* Use A = QR to avoid computing Gussian Elimination
    * $\text{Solve } x = \left( (R^T)R \right)^{-1} (R^T)(Q^T)b \quad \text{which becomes} \quad Rx = (Q^T)b$
* Find and remove the extreme values :
    * $\text{Compute the least square equation:} \quad y = a \cdot x_1 + b \cdot x_2 + c$
    * $e_i = \frac{|y_{\text{given}} - y_{\text{least square}}|}{\sigma}$
    * $\text{Remove the value of index } i \text{ such that } e_i > \text{extremeBound}.$
    * Find the least square equation again

### Test Environment
* The playground is closed now.
* The playground is open to the public and can be edited by anyone, so if you want to test the code, I will open the playground for you to test. [LINK](https://leetcode.com/playground/krTvtUT2)
* All you can edit is the array x1, x2, y, and they MUST have the same length.
![image](https://github.com/user-attachments/assets/d417ccc8-9faf-4279-ae2e-3aec721c03ad)

### JS version:
```js
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

// test environment

class FunctionSet {
    static doubleError = 0.001;
    static extremeBound = 2;

    static run(x1, x2, y) {
        let equation = this.find2VariableCurve(x1, x2, y);
        let extremes = this.findExtreme(x1, x2, y, equation);
        x1 = this.removeExtreme(x1, extremes);
        x2 = this.removeExtreme(x2, extremes);
        y = this.removeExtreme(y, extremes);
        equation = this.find2VariableCurve(x1, x2, y);
        return equation;
    }

    static transpose(arr) {
        const m = arr.length;
        const n = arr[0].length;
        const trans = Array.from({ length: n }, () => Array(m));
        for (let i = 0; i < m; i++) {
            for (let j = 0; j < n; j++) {
                trans[j][i] = arr[i][j];
            }
        }
        return trans;
    }

    static multiply(arr1, arr2) {
        const m1 = arr1.length;
        const n1 = arr1[0].length;
        const m2 = arr2.length;
        const n2 = arr2[0].length;

        if (n1 !== m2) {
            console.error("Error: n1 != m2 at function : multiply");
            return null;
        }

        const result = Array.from({ length: m1 }, () => Array(n2).fill(0));

        for (let i = 0; i < m1; i++) {
            for (let j = 0; j < n2; j++) {
                for (let k = 0; k < m2; k++) {
                    result[i][j] += arr1[i][k] * arr2[k][j];
                }
            }
        }

        return result;
    }

    static isZeroVector(v) {
        for (let i = 0; i < v.length; i++) {
            if (v[i] > this.doubleError) return false;
        }
        return true;
    }

    static GramSchmidtProcess(A) {
        const m = A.length;
        const n = A[0].length;
        let idx = 0;
        const Q = [];
        let v;
        let len;

        // first step
        v = [...A[idx++]]; // error vector (let e1 = v1)
        len = 0;
        for (let i of v) {
            len += i * i;
        }
        len = Math.sqrt(len);
        for (let i = 0; i < n; i++) { // normalization
            v[i] /= len;
        }

        // step iteration
        while (!this.isZeroVector(v)) {
            Q.push(v);

            if (idx >= m) break;

            // find error vector
            v = [...A[idx++]];
            for (let q of Q) { // vk = sigma(k-1, 1)_qi^T vk qi
                let t = 0;
                for (let i = 0; i < q.length; i++) t += q[i] * v[i];
                for (let i = 0; i < v.length; i++) v[i] -= t * q[i];
            }

            // normalization
            len = 0;
            for (let i of v) {
                len += i * i;
            }
            len = Math.sqrt(len);
            for (let i = 0; i < n; i++) { // normalization
                v[i] /= len;
            }
        }

        return Q;
    }

    static QRFactorization(A) {
        const as = [];
        for (let i = 0; i < A[0].length; i++) {
            const t = Array.from({ length: A.length });
            for (let j = 0; j < A.length; j++) {
                t[j] = A[j][i];
            }
            as.push(t);
        }

        const qs = this.GramSchmidtProcess(as);
        const m = qs[0].length;
        const k = qs.length;

        // Q
        const Q = Array.from({ length: m }, () => Array(k));
        for (let i = 0; i < k; i++) {
            for (let j = 0; j < m; j++) {
                Q[j][i] = qs[i][j];
            }
        }

        // R
        const R = Array.from({ length: k }, () => Array(k).fill(0));
        for (let i = 0; i < k; i++) {
            for (let j = i; j < k; j++) {
                for (let t = 0; t < m; t++) {
                    R[i][j] += as[j][t] * qs[i][t];
                }
            }
        }

        return [Q, R];
    }

    static findX(A, b) {
        const QR = this.QRFactorization(A);
        const Q = QR[0];
        const R = QR[1];
        const k = QR[0][0].length;
        const m = A.length;
        const n = A[0].length;

        if (n !== k) {
            console.error("Error: n != k at function : findX");
            return null;
        }

        const c = this.multiply(this.multiply(this.transpose(R), this.transpose(Q)), b);
        const y = Array.from({ length: k }, () => [0]);

        const RT = this.transpose(R);
        for (let i = 0; i < k; i++) {
            let temp = 0;
            for (let j = 0; j < i; j++) {
                temp += RT[i][j] * y[j][0];
            }
            y[i][0] = (c[i][0] - temp) / RT[i][i];
        }

        const x = Array.from({ length: k }, () => [0]);
        for (let i = k - 1; i >= 0; i--) {
            let temp = 0;
            for (let j = k - 1; j > i; j--) {
                temp += R[i][j] * x[j][0];
            }
            x[i][0] = (y[i][0] - temp) / R[i][i];
        }

        return x.map(val => val[0]);
    }

    static find2VariableCurve(x1, x2, y) {
        const A = x1.map((_, i) => [x1[i], x2[i], 1]);
        const b = y.map(val => [val]);

        return this.findX(A, b);
    }

    static findExtreme(x1, x2, y, equation) {
        const e = x1.map((_, i) => Math.abs((equation[0] * x1[i] + equation[1] * x2[i] + equation[2]) - y[i]));
        let sigma = 0;
        let average = e.reduce((acc, val) => acc + val, 0) / e.length;

        sigma = e.reduce((acc, val) => acc + (val - average) * (val - average), 0) / e.length;
        sigma = Math.sqrt(sigma);

        if (sigma < this.doubleError) return [];

        for (let i = 0; i < e.length; i++) e[i] /= sigma;

        const extremes = [];
        for (let i = 0; i < e.length; i++) {
            if (e[i] > this.extremeBound) extremes.push(i);
        }

        return extremes;
    }

    static removeExtreme(x, extremes) {
        return x.filter((_, index) => !extremes.includes(index));
    }
}

// Call the main function to execute
main();
```
