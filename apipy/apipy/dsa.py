        neta[0] = X[0] * W[0] + X[1] * W[8] + X[2] * W[16] + X[3] * W[24] + X[4] * W[32] + X[5] * W[40] + X[6] * W[48] + X[7] * W[56] - U[0]
        neta[1] = X[0] * W[1] + X[1] * W[9] + X[2] * W[17] + X[3] * W[25] + X[4] * W[33] + X[5] * W[41] + X[6] * W[49] + X[7] * W[57] - U[1]
        neta[2] = X[0] * W[2] + X[1] * W[10] + X[2] * W[18] + X[3] * W[26] + X[4] * W[34] + X[5] * W[42] + X[6] * W[50] + X[7] * W[58] - U[2]
        neta[3] = X[0] * W[3] + X[1] * W[11] + X[2] * W[19] + X[3] * W[27] + X[4] * W[35] + X[5] * W[43] + X[6] * W[51] + X[7] * W[59] - U[3]
        neta[4] = X[0] * W[4] + X[1] * W[12] + X[2] * W[20] + X[3] * W[28] + X[4] * W[36] + X[5] * W[44] + X[6] * W[52] + X[7] * W[60] - U[4]
        neta[5] = X[0] * W[5] + X[1] * W[13] + X[2] * W[21] + X[3] * W[29] + X[4] * W[37] + X[5] * W[45] + X[6] * W[53] + X[7] * W[61] - U[5]
        neta[6] = X[0] * W[6] + X[1] * W[14] + X[2] * W[22] + X[3] * W[30] + X[4] * W[38] + X[5] * W[46] + X[6] * W[54] + X[7] * W[62] - U[6]
        neta[7] = X[0] * W[7] + X[1] * W[15] + X[2] * W[23] + X[3] * W[31] + X[4] * W[39] + X[5] * W[47] + X[6] * W[55] + X[7] * W[63] - U[7]

        fneta[0] = f(neta[0])
        fneta[1] = f(neta[1])
        fneta[2] = f(neta[2])
        fneta[3] = f(neta[3])
        fneta[4] = f(neta[4])
        fneta[5] = f(neta[5])
        fneta[6] = f(neta[6])
        fneta[7] = f(neta[7])

        neta[8] = fneta[0] * W[64] + fneta[1] * W[66] + fneta[2] * W[68] + fneta[3] * W[70] + fneta[4] * W[72] + fneta[5] * W[74] + fneta[6] * W[76] + fneta[7] * W[78] - U[8]
        neta[9] = fneta[0] * W[65] + fneta[1] * W[67] + fneta[2] * W[69] + fneta[3] * W[71] + fneta[4] * W[73] + fneta[5] * W[75] + fneta[6] * W[77] + fneta[7] * W[79] - U[9]
    
        fneta[8] = f(neta[8])
        fneta[9] = f(neta[9])