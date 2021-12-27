# IntTurtle System Call Table

IntTurtle turtles run on an intcode VM named *CraftCode*.
This VM adds a new opcode named `syscall` with the value `10`.
It takes a single argument that is read and depending on its value a system call from the table below is executed.
Calling a non-existent system call will crash the VM.

Additional parameters or outputs are commonly passed through the memory at indices `0` to `3`, so make sure, you don't have any important data here.
You can use the code `1101, 0, 0, 0` which will set them all to `0` and continue execution from memory at index `4`.

### Dynamic Object Table

To handle more advanced objects like strings, NBT or inventories, IntTurtle has a system called **d**ynamic **o**bject **t**able or *dot* for short.
There are three built in registers for dot-objects named `X`, `A` and `B`.
Most system calls expect values in the `X` register, some also in `A` or `B`.
To store more dot-objects, a dot object can be allocated using one of the `allocate` system calls that copy a value from one of the registers into an internal table and return an id to access that object later.
If a register holds no value or a value of a different type than expected, it is interpreted to hold the default value for the type expected.

Dot-objects can store the following things:

|      Type       | Default value                               |
|:---------------:|:--------------------------------------------|
|     String      | Empty string                                |
|       NBT       | Empty compound tag                          |
| Block Inventory | Empty inventory with 0 slots and no content |

### Other data

#### Rotations

Rotations are represented as integers:

| Rotation | Value |
|:--------:|:-----:|
|  North   |  `2`  |
|   East   |  `3`  |
|  South   |  `0`  |
|   West   |  `1`  |

#### Directions

Directions for the turtle are represented as integers:

| Direction | Value |
|:---------:|:-----:|
|  Forward  |  `0`  |
|    Up     |  `1`  |
|   Down    |  `2`  |

#### Dimension Ids

Dimension Ids are integer values for vanilla dimensions. Other dimensions need to be compared using their registry name.

|       Dimension        | Value |
|:----------------------:|:-----:|
|       Overworld        |  `0`  |
|         Nether         | `-1`  |
|        The End         |  `1`  |
| Other modded dimension |  `2`  |

#### NBT Tag Types

Tag types are represented as integers:

|  Tag Type  | Value |
|:----------:|:-----:|
|    End     |  `0`  |
|    Byte    |  `1`  |
|   Short    |  `2`  |
|    Int     |  `3`  |
|    Long    |  `4`  |
|   Float    |  `5`  |
|   Double   |  `6`  |
| Byte Array |  `7`  |
|   String   |  `8`  |
|    List    |  `9`  |
|  Compound  | `10`  |
| Int Array  | `11`  |
| Long Array | `12`  |

### System Calls

*Coming Soon*