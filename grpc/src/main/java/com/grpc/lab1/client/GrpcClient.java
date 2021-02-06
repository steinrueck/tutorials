package com.grpc.lab1.client;
import com.grpc.lab1.BlockRequest;

import java.util.Arrays;

import com.grpc.lab1.BlockReply;
import com.grpc.lab1.CalculatorServiceGrpc;
import com.grpc.lab1.BlockRequest.Row;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcClient {
    static int MAX = 4;
    static int[][] multiplyBlock(int A[][], int B[][], CalculatorServiceGrpc.CalculatorServiceBlockingStub stub)
    {
        BlockRequest.Builder requestBuilder = BlockRequest.newBuilder();
        for (int i = 0; i < 4; i++){
            Row.Builder rowBuilderA = Row.newBuilder();
            Row.Builder rowBuilderB = Row.newBuilder();
            for (int j = 0; j < 4; j++) {
                rowBuilderA.addColumns(A[i][j]);
                rowBuilderB.addColumns(B[i][j]);
            }
            requestBuilder.addA(rowBuilderA);
            requestBuilder.addB(rowBuilderB);
        }
        BlockRequest request = requestBuilder.build();
        System.out.println("This is the request from client" + request);
        BlockReply ans = BlockReply.newBuilder().build();
        try {
        ans = stub.multiplyBlock(request);
        } catch (StatusRuntimeException e) {
            System.out.println(e);
        }
        System.out.println("Received reply from server: " + ans);
        System.out.println("Parsing C!");

        int C[][] = new int[MAX][MAX];
        for (int i= 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++) 
            {
                C[i][j] = ans.getC(i).getColumns(j);
            }
        }
        System.out.println("Result parcing C: " + Arrays.deepToString(C));
        return C;
    }
    static int[][] addBlock(int A[][], int B[][], CalculatorServiceGrpc.CalculatorServiceBlockingStub stub)
    {
        BlockRequest.Builder requestBuilder = BlockRequest.newBuilder();
        for (int i = 0; i < 4; i++){
            Row.Builder rowBuilderA = Row.newBuilder();
            Row.Builder rowBuilderB = Row.newBuilder();
            for (int j = 0; j < 4; j++) {
                rowBuilderA.addColumns(A[i][j]);
                rowBuilderB.addColumns(B[i][j]);
            }
            requestBuilder.addA(rowBuilderA);
            requestBuilder.addB(rowBuilderB);
        }
        BlockRequest request = requestBuilder.build();
        System.out.println("This is the request from client" + request);
        BlockReply ans = BlockReply.newBuilder().build();
        try {
            ans = stub.addBlock(request);
        } catch (StatusRuntimeException e) {
            System.out.println(e);
        }

        System.out.println("Received reply from server: " + ans);
        System.out.println("Parsing C!");

        int C[][] = new int[MAX][MAX];
        for (int i= 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                C[i][j] = ans.getC(i).getColumns(j);
            }
        }
        System.out.println("Result parcing C: " + Arrays.deepToString(C));
        return C;
    }
    public static void main(String[] args)
    {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081).usePlaintext().build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);

        int A[][] = { {1, 2, 3, 4}, 
        {5, 6, 7, 8}, 
        {9, 10, 11, 12},
        {13, 14, 15, 16}}; 

        int B[][] = { {1, 2, 3, 4}, 
        {5, 6, 7, 8}, 
        {9, 10, 11, 12},
        {13, 14, 15, 16}};

        int bSize=2;
        int[][] A1 = new int[MAX][MAX];
        int[][] A2 = new int[MAX][MAX];
        int[][] A3 = new int[MAX][MAX];
        int[][] B1 = new int[MAX][MAX];
        int[][] B2 = new int[MAX][MAX];
        int[][] B3 = new int[MAX][MAX];
        int[][] C1 = new int[MAX][MAX];
        int[][] C2 = new int[MAX][MAX];
        int[][] C3 = new int[MAX][MAX];
        int[][] D1 = new int[MAX][MAX];
        int[][] D2 = new int[MAX][MAX];
        int[][] D3 = new int[MAX][MAX];
        int[][] res= new int[MAX][MAX];
        for (int i = 0; i < bSize; i++) 
        { 
            for (int j = 0; j < bSize; j++)
            {
                A1[i][j]=A[i][j];
                A2[i][j]=B[i][j];
            }
        }
        for (int i = 0; i < bSize; i++) 
        { 
            for (int j = bSize; j < MAX; j++)
            {
                B1[i][j-bSize]=A[i][j];
                B2[i][j-bSize]=B[i][j];
            }
        }
        for (int i = bSize; i < MAX; i++)
        {
            for (int j = 0; j < bSize; j++)
            {
                C1[i-bSize][j]=A[i][j];
                C2[i-bSize][j]=B[i][j];
            }
        }
        for (int i = bSize; i < MAX; i++) 
        { 
            for (int j = bSize; j < MAX; j++)
            {
                D1[i-bSize][j-bSize]=A[i][j];
                D2[i-bSize][j-bSize]=B[i][j];
            }
        }
        System.out.println(Arrays.deepToString(A1));
        System.out.println(Arrays.deepToString(A2));
        System.out.println(Arrays.deepToString(B1));
        System.out.println(Arrays.deepToString(C2));

        A3=addBlock(multiplyBlock(A1,A2,stub),multiplyBlock(B1,C2,stub),stub);
        B3=addBlock(multiplyBlock(A1,B2,stub),multiplyBlock(B1,D2,stub),stub);
        C3=addBlock(multiplyBlock(C1,A2,stub),multiplyBlock(D1,C2,stub),stub);
        D3=addBlock(multiplyBlock(C1,B2,stub),multiplyBlock(D1,D2,stub),stub);
        for (int i = 0; i < bSize; i++) 
        { 
            for (int j = 0; j < bSize; j++)
            {
                res[i][j]=A3[i][j];
            }
        }
        for (int i = 0; i < bSize; i++) 
        { 
            for (int j = bSize; j < MAX; j++)
            {
                res[i][j]=B3[i][j-bSize];
            }
        }
        for (int i = bSize; i < MAX; i++) 
        { 
            for (int j = 0; j < bSize; j++)
            {
                res[i][j]=C3[i-bSize][j];
            }
        } 
        for (int i = bSize; i < MAX; i++) 
        { 
            for (int j = bSize; j < MAX; j++)
            {
                res[i][j]=D3[i-bSize][j-bSize];
            }
        } 
        for (int i=0; i<MAX; i++)
        {
            for (int j=0; j<MAX;j++)
            {
                System.out.print(res[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("Final Answer");
        System.out.println(Arrays.deepToString(res));
        channel.shutdown(); 
    }
}