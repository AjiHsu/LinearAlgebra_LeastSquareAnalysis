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
    static extremeBound = 2.0;

    constructor() {
        // cannot create an object
        throw new Error("This class cannot be instantiated.");
    }

    static run(x1, x2, y) {
        let equation = this.find2VariableCurve(x1, x2, y);
        const extremes = this.findExtreme(x1, x2, y, equation);
        x1 = this.removeExtreme(x1, extremes);
        x2 = this.removeExtreme(x2, extremes);
        y = this.removeExtreme(y, extremes);
        equation = this.find2VariableCurve(x1, x2, y);
        return equation;
    }

    static transpose(arr) {
        const m = arr.length;
        const n = arr[0].length;
        const trans = Array.from({ length: n }, () => Array(m).fill(0));

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
        return v.every(value => Math.abs(value) <= this.doubleError);
    }

    static GramSchmidtProcess(A) {
        const m = A.length; // row number
        const n = A[0].length; // Rn
        let idx = 0;
        const Q = [];
        let v;
        let len;

        // first step
        v = [...A[idx++]]; // error vector (let e1 = v1)
        len = Math.sqrt(v.reduce((sum, i) => sum + i * i, 0));

        v = v.map(i => i / len); // normalization

        // step iteration
        while (!this.isZeroVector(v)) {
            Q.push(v); // add to Q

            if (idx >= m) break;

            // find error vector
            v = [...A[idx++]];
            for (const q of Q) {
                const t = q.reduce((sum, qi, i) => sum + qi * v[i], 0);
                for (let i = 0; i < v.length; i++) {
                    v[i] -= t * q[i];
                }
            }

            // normalization
            len = Math.sqrt(v.reduce((sum, i) => sum + i * i, 0));
            v = v.map(i => i / len);
        }

        return Q;
    }

    static QRFactorization(A) { // get(0) = Q; get(1) = R
        const as = Array.from({ length: A[0].length }, (_, i) => A.map(row => row[i]));

        const qs = this.GramSchmidtProcess(as); // orthonormal column vectors : q
        const m = qs[0].length; // dim of column vectors
        const k = qs.length; // number of orthonormal vectors

        // Q
        const Q = Array.from({ length: m }, (_, j) => qs.map(q => q[j]));

        // R
        const R = Array.from({ length: k }, () => Array(k).fill(0));
        for (let i = 0; i < k; i++) {
            for (let j = i; j < k; j++) {
                R[i][j] = qs[i].reduce((sum, qi, t) => sum + qi * as[j][t], 0);
            }
        }

        return [Q, R];
    }

    static findX(A, b) { // b is column vector {{}, {}, {} ...}
        const [Q, R] = this.QRFactorization(A);
        const c = this.multiply(this.transpose(Q), b);

        const x = Array(R.length).fill(0);
        for (let i = R.length - 1; i >= 0; i--) {
            let temp = 0;
            for (let j = R.length - 1; j > i; j--) {
                temp += R[i][j] * x[j];
            }
            x[i] = (c[i][0] - temp) / R[i][i];
        }

        return x;
    }

    static find2VariableCurve(x1, x2, y) {
        // y = ax1 + bx2 + c
        const A = x1.map((val, i) => [x1[i], x2[i], 1]);
        const b = y.map(val => [val]);

        return this.findX(A, b);
    }

    static findExtreme(x1, x2, y, equation) { // equation { a, b, c } where y = ax1 + bx2 + c
        const e = x1.map((_, i) =>
            Math.abs((equation[0] * x1[i] + equation[1] * x2[i] + equation[2]) - y[i])
        );

        // find sigma
        const average = e.reduce((sum, val) => sum + val, 0) / e.length;
        let sigma = Math.sqrt(
            e.reduce((sum, val) => sum + (val - average) ** 2, 0) / e.length
        );
        if (sigma < this.doubleError) return [];

        // find extreme
        const normalizedE = e.map(val => val / sigma);

        return normalizedE
            .map((val, i) => (val > this.extremeBound ? i : -1))
            .filter(index => index !== -1);
    }

    static removeExtreme(arr, extremes) {
        return arr.filter((_, i) => !extremes.includes(i));
    }
}

// Call the main function to execute
main();
```
