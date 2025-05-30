# SWEN90004-2025-Assignment2-Group41

# Wealth Distribution Simulation

This project implements a Java-based simulation of the NetLogo Wealth Distribution model, automating simulations for three scenarios of different reproduction rules, population sizes, and grain growth rates. Each case in scenarios runs 10 times, and results are saved as CSV files in the `/stat` folder. A Python script is provided to process the results.


## Prerequisites

- **Java**: JDK 8 or higher installed.
- **Python**: Python 3.6 or higher with `pandas` installed (for processing results).
- **Operating System**: Compatible with Windows, not tested on macOS and Linux (should work).

## Setup

1. **Clone or Download**: Clone the repository or download the project files.
2. **Directory Structure**:
   - `src/Main.java`: The main Java program.
   - `stat/`: Output folder for CSV files.
   - `stat/process_results.py`: Python script to process CSV files.
3. **Install Python Dependencies**:
   ```bash
   pip install pandas
   ```

## Running the Simulation

The simulation is automated and runs from the `main` method in `Main.java`. It executes three scenarios, each with 10 runs, varying:
- **Reproduction Rules**:
  - **Default**: New agents spawn at the same location as their parent, with no wealth inheritance.
  - **Random Spawn**: New agents spawn at random locations, with no wealth inheritance.
  - **Inheritance**: New agents spawn at the same location as their parent and inherit all of their parent’s wealth.
- **Population Size**: Different numbers of agents (100, 250, 500, 1000).
- **Grain Growth Rate**: Different rates of grain regeneration (every 1, 10, 100, 200 ticks).

### Steps to Run

1. **Compile and Run the Java Program**:
   - Navigate to the project root directory.
   - Compile the Java code:
     ```bash
     javac src/Main.java
     ```
   - Run the program:
     ```bash
     java -cp src Main
     ```
   - Or run in IDE

   - The simulation will automatically run all scenarios, producing CSV files in the `/stat` folder.

2. **Output Files**:
   - Results are saved in `/stat` with filenames like `[case].csv`.
   - Each CSV file contains:
     - Columns: `tick,totalWealth,minWealth,maxWealth,avgWealth,gini`
     - Format: Comma-separated values without headers, e.g.:
       ```
       1,984.00,8.50,12.30,9.84,0.1234
       2,976.50,8.20,12.80,9.77,0.1256
       ...
       ```

3. **Process Results**:
   - Navigate to the `/stat` folder:
     ```bash
     cd stat
     ```
   - Run the Python script to process the CSV files:
     ```bash
     python process_results.py
     ```
   - The script aggregates results (e.g., averages Gini coefficients across runs) and produce plots for visualizations.

## Notes

- **Simulation Parameters**: The scenarios vary population sizes and grain growth rates. Exact values are defined in `/world/Simulation.java` (e.g., population sizes: 100, 250, 500, 1000; grain growth rates: 1, 10, 50, 100).
- **Python Script**: The `process_results.py` script is assumed to handle tasks like averaging metrics and generating plots. 
- **Performance**: The simulation runs sequentially with fixed agent order per tick, mimicking NetLogo’s model. For large populations or long runs, monitor runtime and disk space for CSV files.

## Troubleshooting

- **File Permission Errors**: Ensure the `/stat` folder is writable. If issues occur, check file permissions or specify an absolute path in the Java code.
- **Missing Python Dependencies**: Install `pandas` using `pip install pandas` if the Python script fails.
- **Java Compilation Errors**: Verify JDK is installed and the `src` directory contains all the java files.

## References

- [NetLogo Wealth Distribution Model](https://ccl.northwestern.edu/netlogo/models/WealthDistribution)
- [NetLogo Programming Guide](https://ccl.northwestern.edu/netlogo/docs/programming.html)
