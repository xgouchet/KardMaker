# KardMaker

KardMaker is a command-line tool to generate card images (as PNG files) for board game prototypes and other projects from a simple JSON definition file.

## Features

*   **Template-Based**: Define a base template for your cards, including dimensions, bleed/safe areas, and resolution.
*   **Dynamic Elements**: Position text, images, and basic shapes on your cards.
*   **Flexible Font Support**: Use custom `.ttf` or `.otf` fonts from an `input/` directory, or reference any font installed on your system by name.
*   **Batch Processing**: Generate cards from a single file or process an entire folder of JSON definitions at once.

## Prerequisites

*   Java Runtime Environment (JRE) 1.8 or higher.

## Usage

The easiest way to use KardMaker is to run it via the included Gradle wrapper.

The tool takes one required argument: the path to your JSON configuration file. It also supports several optional flags:
```bash
./gradlew run --args="<options> <path/to/your/cards.json>"
```

**Example:**
```bash
# Generate cards from a single file
./gradlew run --args="my_cards.json"

# Generate cards from all .json files in a directory
./gradlew run --args="--folder my_card_set/"

# Get more detailed output
./gradlew run --args="--verbose my_cards.json"
```

### Options

*   `-v`, `--verbose`: Enables detailed logging to the console.
*   `-f`, `--folder`: Treats the input path as a directory and processes all `.json` files inside it.
*   `-d`, `--debug`: Draws the cut and safe area rectangles on the generated cards, which is useful for layout design.

### Output

Generated cards will be saved as `.png` files inside an `output/` directory, which is created relative to where the command is run.

## JSON Configuration

The JSON file has two main parts: the `template` and the `cards` array.

### 1. The `template` Object

This object defines the physical properties and shared elements of your cards.

| Property   | Type   | Description                                                                                                   |
| :--------- | :----- | :------------------------------------------------------------------------------------------------------------ |
| `width`    | Number | The width of the card in millimeters.                                                                         |
| `height`   | Number | The height of the card in millimeters.                                                                        |
| `cut`      | Number | The thickness of the bleed area (in mm) from the edge of the card to the cut line.                            |
| `safe`     | Number | The thickness of the safe area margin (in mm) from the cut line inwards.                                      |
| `dpi`      | Number | The dots-per-inch resolution for the output PNG image. `300` is standard for printing.                        |
| `elements` | Array  | An array of objects that define the common elements to be drawn on every card (e.g., a shared background). |

### 2. The `elements` Array

Each object in this array represents a single item to be drawn on the card. You can define `elements` in both the main `template` and within each individual `card` object.

The main supported element types are:
*   **`text`**: Draws a line of text.
*   **`image`**: Draws a bitmap image.
*   **`rect`**: Draws a filled rectangle.
*   **`oval`**: Draws a filled oval or circle.
*   **`line`**: Draws a simple line.

Each element has common properties (`id`, `x`, `y`, `width`, `height`) and type-specific properties (e.g., `text` and `font` for a `text` element, or `color` for a `rect`).

### 3. The `cards` Array

This array defines the individual cards to be generated. Each object can contain:

| Property   | Type   | Description                                                                     |
| :--------- | :----- | :------------------------------------------------------------------------------ |
| `name`     | String | The name of the card, used for the output filename (e.g., "MyFirstCard").       |
| `elements` | Array  | An array of element objects specific to this card, which can add to or override template elements. |
| `variants` | Object | An object where you can define variations of this card (e.g., different art or stats). |
| `data`     | Object | A key-value map for replacing variables within element properties (e.g., `"{card_title}"`). |

## Full Example (`my_cards.json`)
```json
{
  "template": {
    "width": 63,
    "height": 88,
    "cut": 3,
    "safe": 3,
    "dpi": 72,
    "elements": [
      {
        "type": "rect",
        "id": "background",
        "x": 0,
        "y": 0,
        "width": 63,
        "height": 88,
        "color": "#CCCCCC"
      },
      {
        "type": "image",
        "id": "shared_icon",
        "x": 5,
        "y": 5,
        "width": 10,
        "height": 10,
        "src": "shared_icon.png"
      }
    ]
  },
  "cards": [
    {
      "name": "FirstCard",
      "elements": [
        {
          "type": "text",
          "id": "title",
          "x": 10,
          "y": 20,
          "width": 43,
          "height": 15,
          "text": "My First Card",
          "font": "Arial",
          "size": 12,
          "color": "#000000"
        }
      ]
    },
    {
      "name": "SecondCard",
      "variants": {
        "art": "alternate_art.png"
      },
      "elements": [
        {
          "type": "text",
          "id": "title",
          "x": 10,
          "y": 20,
          "width": 43,
          "height": 15,
          "text": "The Second Card",
          "font": "sans-serif",
          "size": 12,
          "color": "#FFFFFF"
        },
        {
          "type": "image",
          "id": "card_art",
          "x": 10,
          "y": 40,
          "width": 43,
          "height": 30,
          "src": "{art}"
        }
      ]
    }
  ]
}
```

## Contributing

Contributions are welcome! If you find a bug or have a feature request, please open an issue. If you'd like to contribute code, please open a pull request.
