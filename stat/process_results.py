import csv
import os
from collections import defaultdict
import matplotlib.pyplot as plt

# --- Ensure plots directory exists ---
plot_dir = "plots"
os.makedirs(plot_dir, exist_ok=True)

# --- Hardcoded Input/Output File Paths ---
input_output_pairs = [
    ("Default.csv", "Default_Processed.csv"),
    ("RandomSpawn.csv", "RandomSpawn_Processed.csv"),
    ("Inheritance.csv", "Inheritance_Processed.csv"),
    ("GrowthRate_1.csv", "GrowthRate_1_Processed.csv"),
    ("GrowthRate_10.csv", "GrowthRate_10_Processed.csv"),
    ("GrowthRate_50.csv", "GrowthRate_50_Processed.csv"),
    ("GrowthRate_100.csv", "GrowthRate_100_Processed.csv"),
    ("Population_100.csv", "Population_100_Processed.csv"),
    ("Population_250.csv", "Population_250_Processed.csv"),
    ("Population_500.csv", "Population_500_Processed.csv"),
    ("Population_1000.csv", "Population_1000_Processed.csv"),
]

def process_file(input_file, output_file):
    data_by_tick = defaultdict(list)
    tick_interval = 10000

    # Read and group by tick % 6900
    with open(input_file, newline='') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            tick = int(row[0]) % tick_interval
            values = list(map(float, row[1:]))
            data_by_tick[tick].append(values)

    # Write output CSV
    with open(output_file, 'w', newline='') as out_csv:
        writer = csv.writer(out_csv)
        writer.writerow(['tick', 'totalWealth', 'minWealth', 'maxWealth', 'avgWealth', 'gini'])

        for tick in sorted(data_by_tick.keys()):
            rows = data_by_tick[tick]
            total_wealth = sum(r[0] for r in rows) / len(rows)
            min_wealth = min(r[1] for r in rows) / len(rows)
            max_wealth = max(r[2] for r in rows) / len(rows)
            avg_wealth = sum(r[3] for r in rows) / len(rows)
            avg_gini = sum(r[4] for r in rows) / len(rows)

            writer.writerow([tick, round(total_wealth, 2), round(min_wealth, 2),
                             round(max_wealth, 2), round(avg_wealth, 2), round(avg_gini, 4)])

def plot_output(output_file, input_filename_base):
    ticks = []
    total_wealths = []
    avg_wealths = []
    ginis = []

    with open(output_file, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            ticks.append(int(row['tick']))
            total_wealths.append(float(row['totalWealth']))
            avg_wealths.append(float(row['avgWealth']))
            ginis.append(float(row['gini']))

    fig, ax1 = plt.subplots()

    ax1.set_xlabel('Tick')
    ax1.set_ylabel('Total Wealth / Avg Wealth', color='tab:blue')
    ax1.plot(ticks, total_wealths, label='Total Wealth', color='tab:blue', linestyle='-')
    ax1.plot(ticks, avg_wealths, label='Avg Wealth', color='tab:blue', linestyle='--')
    ax1.tick_params(axis='y', labelcolor='tab:blue')

    ax2 = ax1.twinx()
    ax2.set_ylabel('Gini Coefficient', color='tab:red')
    ax2.plot(ticks, ginis, label='Gini', color='tab:red', linestyle='-.')
    ax2.tick_params(axis='y', labelcolor='tab:red')

    fig.tight_layout()
    plt.title(f"Wealth and Gini over Time: {input_filename_base}")
    fig.legend(loc="lower right")

    # Save plot to file
    plot_path = os.path.join(plot_dir, f"{input_filename_base}_plot.png")
    plt.savefig(plot_path)
    plt.close()
    print(f"Saved plot to {plot_path}")

# --- Main Execution ---
for input_file, output_file in input_output_pairs:
    base_name = os.path.splitext(os.path.basename(input_file))[0]
    process_file(input_file, output_file)
    plot_output(output_file, base_name)