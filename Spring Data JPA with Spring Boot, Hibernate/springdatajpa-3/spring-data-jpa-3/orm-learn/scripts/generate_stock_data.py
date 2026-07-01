"""
generate_stock_data.py
─────────────────────────────────────────────────────────────────────────────
Helper script that replaces the manual "Excel CONCATENATE formula" step
described in Hands-on 2 of the PDF.

Instead of opening stock-data.csv in Excel and dragging a formula down,
run this script to generate stock-data.sql directly.

USAGE
-----
    python generate_stock_data.py stock-data.csv stock-data.sql

Expected CSV columns (same order as the hands-on doc):
    A: Date        (e.g. 2018-10-18 or 10/18/2018)
    B: Symbol/Code  (e.g. FB, GOOGL, NFLX)
    C: Open
    D: Close
    E: Volume

If your CSV has a different column order/header, adjust COLUMN_MAP below.

After running this script, load the generated SQL file into MySQL:
    mysql -u root -p ormlearn < stock-data.sql
─────────────────────────────────────────────────────────────────────────────
"""

import csv
import sys
from datetime import datetime


COLUMN_MAP = {
    "date": "Date",
    "code": "Symbol",
    "open": "Open",
    "close": "Close",
    "volume": "Volume",
}

DATE_FORMATS = ["%Y-%m-%d", "%m/%d/%Y", "%d-%m-%Y"]


def parse_date(value: str) -> str:
    for fmt in DATE_FORMATS:
        try:
            d = datetime.strptime(value.strip(), fmt)
            return d.strftime("%Y-%m-%d")
        except ValueError:
            continue
    raise ValueError(f"Unrecognized date format: {value}")


def main():
    if len(sys.argv) != 3:
        print("Usage: python generate_stock_data.py <input_csv> <output_sql>")
        sys.exit(1)

    input_csv, output_sql = sys.argv[1], sys.argv[2]

    with open(input_csv, newline="", encoding="utf-8") as f_in, \
         open(output_sql, "w", encoding="utf-8") as f_out:

        reader = csv.DictReader(f_in)
        f_out.write("USE `ormlearn`;\n\n")

        count = 0
        for row in reader:
            date_str = parse_date(row[COLUMN_MAP["date"]])
            code = row[COLUMN_MAP["code"]].strip()
            open_price = row[COLUMN_MAP["open"]].strip()
            close_price = row[COLUMN_MAP["close"]].strip()
            volume = row[COLUMN_MAP["volume"]].strip()

            f_out.write(
                f"INSERT INTO stock (st_code, st_date, st_open, st_close, st_volume) "
                f"VALUES ('{code}', '{date_str}', {open_price}, {close_price}, {volume});\n"
            )
            count += 1

        print(f"Generated {count} insert statements -> {output_sql}")


if __name__ == "__main__":
    main()
