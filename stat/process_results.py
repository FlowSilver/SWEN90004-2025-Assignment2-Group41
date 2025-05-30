import csv
import os
from collections import defaultdict
import matplotlib.pyplot as plt

# --- Ensure plots directory exists ---
plot_dir = "plots"
os.makedirs(plot_dir, exist_ok=True) # Create 'plots' directory if it doesn't exist

# --- Hardcoded list of input and output CSV file pairs ---
# When changing Simulation.java, please changing this as well
# Each tuple contains the input CSV filename and the corresponding processed output CSV filename
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
    """
    Process the input CSV file by grouping rows by tick % tick_interval,
    then calculate summary statistics per tick_interval and write them to output CSV.

    Args:
        input_file (str): Path to the input CSV file.
        output_file (str): Path where the processed CSV will be saved.
    """
    data_by_tick = defaultdict(list)

    # Tick_Interval, when changing this, also changing MAX_TICK in Params.java
    tick_interval = 10000
    # Read input CSV and group data by tick % tick_interval
    with open(input_file, newline='') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            tick = int(row[0]) % tick_interval
            values = list(map(float, row[1:]))
            data_by_tick[tick].append(values)

    # Write summary statistics to output CSV
    with open(output_file, 'w', newline='') as out_csv:
        writer = csv.writer(out_csv)
        # Write header row
        writer.writerow(['tick', 'totalWealth', 'gini', 'numLowerClass', 'numMiddleClass', 'numUpperClass'])

         # Calculate statistics for each tick and write to CSV
        for tick in sorted(data_by_tick.keys()):
            rows = data_by_tick[tick]

            # Calculate mean statistics over all rows for this tick
            total_wealth = sum(r[0] for r in rows) / len(rows)
            avg_gini = sum(r[1] for r in rows) / len(rows)
            num_lower_class = sum(int(r[2]) for r in rows) // len(rows)
            num_middle_class = sum(int(r[3]) for r in rows) // len(rows)
            num_upper_class = sum(int(r[4]) for r in rows) // len(rows)

            # Write a row of aggregated data for this tick
            writer.writerow([tick, round(total_wealth, 2), avg_gini, num_lower_class, num_middle_class, num_upper_class])

def plot_output(output_file, input_filename_base):
    """
    Generate and save a plot from the processed output CSV file showing
    class distribution and Gini coefficient over ticks.

    Args:
        output_file (str): Path to the processed CSV file.
        input_filename_base (str): Base name used to label and save plot files.
    """
    ticks = []
    ginis = []
    num_lower_class = []
    num_middle_class = []
    num_upper_class = []

    # Read processed CSV file using DictReader for convenience
    with open(output_file, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            ticks.append(int(row['tick']))
            ginis.append(float(row['gini']))
            num_lower_class.append(int(row['numLowerClass']))
            num_middle_class.append(int(row['numMiddleClass']))
            num_upper_class.append(int(row['numUpperClass']))


    # Create plot with Gini index:
    # y-axis for Gini coefficient
    fig, ax1 = plt.subplots()

    ax1.set_xlabel('Tick')
    ax1.set_ylabel('Gini', color='tab:gray')
    ax1.plot(ticks, ginis, label='Total Wealth', color='tab:blue', linestyle='-')
    ax1.tick_params(axis='y', labelcolor='tab:blue')

    fig.tight_layout()
    plt.title(f"Gini over Time: {input_filename_base}")
    fig.legend(loc="lower right")

    # Save plot image to 'plots' directory
    plot_path = os.path.join(plot_dir, f"{input_filename_base}_gini_plot.png")
    plt.savefig(plot_path)
    plt.close()
    print(f"Saved plot to {plot_path}")

    # Create plot for class distribution:
    fig, ax1 = plt.subplots()

    ax1.set_xlabel('Tick')
    ax1.set_ylabel('Turtles', color='tab:gray')
    ax1.plot(ticks, num_lower_class, label='low', color='tab:red', linestyle='-')
    ax1.plot(ticks, num_middle_class, label='mid', color='tab:green', linestyle='-')
    ax1.plot(ticks, num_upper_class, label='up', color='tab:blue', linestyle='-')
    ax1.tick_params(axis='y', labelcolor='tab:gray')

    fig.tight_layout()
    plt.title(f"Class Plot: {input_filename_base}")
    fig.legend(loc="upper right")

    # Save plot image to 'plots' directory
    plot_path = os.path.join(plot_dir, f"{input_filename_base}_class_plot.png")
    plt.savefig(plot_path)
    plt.close()
    print(f"Saved plot to {plot_path}")


# --- Main execution loop ---
# Process each pair of input/output files, generate processed CSV and plot
for input_file, output_file in input_output_pairs:
    base_name = os.path.splitext(os.path.basename(input_file))[0] # Get filename without extension
    process_file(input_file, output_file) # Process and generate processed CSV
    plot_output(output_file, base_name)  # Generate and save plot for processed data