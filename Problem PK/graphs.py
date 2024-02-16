# wykresy do zadania

import matplotlib.pyplot as plt


def plot(x, Y_lock, Y_cond, measurement_title=None, xlabel=None):
    fig, ax = plt.subplots(2, 2, figsize=(16, 9))
    fig.suptitle(measurement_title, fontsize=16)

    # Pierwszy wykres
    ax[0, 0].plot(x, Y_lock[0], 'r.--', x, Y_cond[0], 'b.--')
    ax[0, 0].set_xticks(x)
    ax[0, 0].set_xlabel(xlabel)
    ax[0, 0].set_ylabel("CPU time [ns]")
    ax[0, 0].set_title("CPU time comparison")
    ax[0, 0].legend(["3 locks", "4 conditions"])

    # Drugi wykres
    ax[1, 0].plot(x, Y_lock[1], 'r.--', x, Y_cond[1], 'b.--')
    ax[1, 0].set_xticks(x)
    ax[1, 0].set_xlabel(xlabel)
    ax[1, 0].set_ylabel("Total number of operations")
    ax[1, 0].set_title("CPU number of operations comparison")
    ax[1, 0].legend(["3 locks", "4 conditions"])

    # Trzeci wykres
    ax[1, 1].plot(x, Y_lock[2], 'r.--', x, Y_cond[2], 'b.--')
    ax[1, 1].set_xticks(x)
    ax[1, 1].set_xlabel(xlabel)
    ax[1, 1].set_ylabel("Total number of await() calls")
    ax[1, 1].set_title("Number of await() calls comparison")
    ax[1, 1].legend(["3 locks", "4 conditions"])

    # Czwarty wykres
    ax[0, 1].plot(x, Y_cond[3], 'r.--', x, Y_cond[4], 'b.--')
    ax[0, 1].set_xticks(x)
    ax[0, 1].set_xlabel(xlabel)
    ax[0, 1].set_ylabel("Total number of await() calls")
    ax[0, 1].set_title("Threads on first/rest queue comparison (4 cond)")
    ax[0, 1].legend(["first queue", "rest queue"])

    plt.tight_layout()
    plt.savefig("graphs/" + measurement_title)


def read(file1_path, file2_path):
    with open(file1_path, 'r') as f1, open(file2_path, 'r') as f2:
        cpu_time_3l = []
        cpu_count_3l = []
        await_count_3l = []

        data = f1.read().split('\n')
        for line in data:
            if line == '':
                continue
            line = line.split(' ')

            cpu_time_3l.append(int(line[1]))
            cpu_count_3l.append(int(line[2]))
            await_count_3l.append(int(line[3]))

        cpu_time_4c = []
        cpu_count_4c = []
        first_await_count_4c = []
        rest_await_count_4c = []
        await_count_4c = []

        data = f2.read().split('\n')
        for line in data:
            if line == '':
                continue
            line = line.split(' ')

            cpu_time_4c.append(int(line[1]))
            cpu_count_4c.append(int(line[2]))
            first_await_count_4c.append(int(line[3]))
            rest_await_count_4c.append(int(line[4]))
            await_count_4c.append(int(line[3]) + int(line[4]))

        return (
            (cpu_time_3l, cpu_count_3l, await_count_3l),
            (cpu_time_4c, cpu_count_4c, await_count_4c,
             first_await_count_4c, rest_await_count_4c)
        )


# 1. zmienna liczba wątków
Y_lock, Y_cond = read('res/Thread3L.txt', 'res/Thread4C.txt')
x_thread = [2*i for i in range(1, 11)]
plot(x_thread, Y_lock, Y_cond, "VARIABLE NUMBER OF THREADS", "Number of threads")


# 2. zmienny rozmiar buffora
Y_lock, Y_cond = read('res/Buff3L.txt', 'res/Buff4C.txt')
buff_size = [i for i in range(50, 1000, 100)]
plot(buff_size, Y_lock, Y_cond, "VARIABLE BUFFER SIZE", "Buff size")

# 3. buffor się zmienia, ale max portion nie
Y_lock, Y_cond = read('res/FixedPor3L.txt', 'res/FixedPor4C.txt')
buff_size = [i for i in range(50, 1000, 100)]
plot(buff_size, Y_lock, Y_cond,
     "VARIABLE BUFFER SIZE WITH FIXED MAX_PORTION", "Buff size")

# 4.zmienna max portion (buff=1000)
Y_lock, Y_cond = read('res/Por3L.txt', 'res/Por4C.txt')
buff_size = [i for i in range(50, 500, 50)]
plot(buff_size, Y_lock, Y_cond,
     "VARIABLE MAX_PORTION WITH FIXED BUFFER SIZE (1000)", "max_portion")

# 5. zmienna ilość czasu życia wątku
Y_lock, Y_cond = read('res/Tlt3L.txt', 'res/Tlt4C.txt')
buff_size = [i for i in range(500, 11000, 1000)]
plot(buff_size, Y_lock, Y_cond,
     "VARIABLE THREAD LIFESPAN", "Thread lifespan [ms]")

# 6.zmienna liczba konsumentów przy jednym producencie

Y_lock, Y_cond = read('res/Prod3L.txt', 'res/Prod4C.txt')
buff_size = [i for i in range(1, 11)]
plot(buff_size, Y_lock, Y_cond,
     "VARIABLE NUMBER OF CONSUMERS WITH ONE PRODUCER", "Number of consumers")
