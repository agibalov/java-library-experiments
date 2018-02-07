package me.loki2302;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;

public class App {
    public static void main(String[] args) throws HDF5Exception {
        writeArrayOfIntsToHDF5File("1.h5", new int[] { 111, 222, 333 });
        int[] values = readArrayOfIntsFromHDF5File("1.h5");

        for(int value : values) {
            System.out.println(value);
        }
    }

    public static int[] readArrayOfIntsFromHDF5File(String fileName) throws HDF5Exception {
        int fileId = 0;
        try {
            fileId = H5.H5Fopen(
                    fileName,
                    HDF5Constants.H5F_ACC_RDONLY,
                    HDF5Constants.H5P_DEFAULT);

            int datasetId = 0;
            try {
                datasetId = H5.H5Dopen(fileId, "/data", HDF5Constants.H5P_DEFAULT);

                int dataspaceId = 0;
                try {
                    dataspaceId = H5.H5Dget_space(datasetId);
                    int numberOfExtents = H5.H5Sget_simple_extent_ndims(dataspaceId);
                    System.out.printf("Number of extents: %d\n", numberOfExtents);

                    if(numberOfExtents != 1) {
                        throw new RuntimeException("Expected to have 1-D array");
                    }

                    long[] dimensions = new long[numberOfExtents];
                    H5.H5Sget_simple_extent_dims(dataspaceId, dimensions, null);
                    for(int extent = 0; extent < numberOfExtents; ++extent) {
                        System.out.printf("  Extent %d size is %d\n", extent, dimensions[extent]);
                    }

                    int dimension = (int)dimensions[0];
                    int[] values = new int[dimension];
                    H5.H5Dread(
                            datasetId,
                            HDF5Constants.H5T_NATIVE_INT,
                            HDF5Constants.H5S_ALL,
                            HDF5Constants.H5S_ALL,
                            HDF5Constants.H5P_DEFAULT,
                            values);

                    return values;
                } finally {
                    if(dataspaceId != 0) {
                        H5.H5Sclose(dataspaceId);
                    }
                }
            } finally {
                if(datasetId != 0) {
                    H5.H5Dclose(datasetId);
                }
            }
        } finally {
            if(fileId != 0) {
                H5.H5Fclose(fileId);
            }
        }
    }

    public static void writeArrayOfIntsToHDF5File(String fileName, int[] values) throws HDF5Exception {
        int fileId = 0;
        try {
            fileId = H5.H5Fcreate(
                    fileName,
                    HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT,
                    HDF5Constants.H5P_DEFAULT);

            int dataspaceId = 0;
            try {
                int numberOfValues = values.length;
                dataspaceId = H5.H5Screate_simple(1, new long[] { numberOfValues }, null);

                int datasetId = 0;
                try {
                    datasetId = H5.H5Dcreate(
                            fileId,
                            "/data",
                            HDF5Constants.H5T_STD_I32BE,
                            dataspaceId,
                            HDF5Constants.H5P_DEFAULT,
                            HDF5Constants.H5P_DEFAULT,
                            HDF5Constants.H5P_DEFAULT);

                    H5.H5Dwrite(
                            datasetId,
                            HDF5Constants.H5T_NATIVE_INT,
                            HDF5Constants.H5S_ALL,
                            HDF5Constants.H5S_ALL,
                            HDF5Constants.H5P_DEFAULT,
                            values);
                } finally {
                    if(datasetId != 0) {
                        H5.H5Dclose(datasetId);
                    }
                }
            } finally {
                if(dataspaceId != 0) {
                    H5.H5Sclose(dataspaceId);
                }
            }
        } finally {
            if(fileId != 0) {
                H5.H5Fclose(fileId);
            }
        }
    }
}
