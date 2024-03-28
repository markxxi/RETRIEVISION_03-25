import webcolors
from colorthief import ColorThief
from PIL import Image

def get_colors(image_file, numcolors=5, resize=150):
    # Open the image and resize it
    img = Image.open(image_file)
    img.thumbnail((resize, resize))

    # Get the dominant color and color palette
    ct = ColorThief(image_file)
    palette = ct.get_palette(color_count=numcolors)

    hex_formatted_palette = []
    closest_color_names = []
    parent_color_names = []
    for color in palette:
        try:
            color_name = webcolors.rgb_to_name(color)
            # print(f"The Color is exactly {color_name}")
        except ValueError:
            color_name = get_closest_color_name(color)
            # print(f"The Color is closest to {color_name}")

        formatted_color = '#{0:02x}{1:02x}{2:02x}'.format(*color)
        hex_formatted_palette.append(formatted_color)

        closest_color_names.append(color_name)

        parent_color = identify_parent_color(color_name)
        parent_color_names.append(parent_color)
        # print (f"{color_name} aka ({parent_color}) with hex value: {formatted_color}")

    # Determine the color family of the dominant color
    # parent_color = identify_parent_color(dominant_color)
    # return f"Palette: {closest_color_names} aka ({parent_color_names}) with hex value:", hex_formatted_palette
    return closest_color_names


def get_closest_color_name(rgb):
    differences = {}
    for color_hex, color_name in webcolors.CSS3_HEX_TO_NAMES.items():
        r, g, b = webcolors.hex_to_rgb(color_hex)
        differences[sum([(r - rgb[0]) ** 2,
                         (g - rgb[1]) ** 2,
                         (b - rgb[2]) ** 2])] = color_name
    return differences[min(differences.keys())]

def identify_parent_color(cname):
    distinctive_colors = {
        'white': ['white', 'snow', 'seashell', 'antiquewhite', 'floralwhite', 'ivory', 'honeydew',
                  'mintcream', 'azure', 'aliceblue', 'ghostwhite', 'lavenderblush'],
        'gray': ['dimgray', 'dimgrey', 'gray', 'grey', 'darkgray', 'darkgrey', 'silver',
                 'lightgray', 'lightgrey', 'gainsboro', 'whitesmoke'],
        'red': ['orangered', 'coral', 'darksalmon', 'tomato', 'salmon', 'mistyrose', 'red',
                'darkred', 'maroon', 'firebrick', 'indianred', 'lightcoral'],
        'brown': ['linen', 'peru', 'peachpuff', 'sandybrown', 'saddlebrown', 'chocolate', 'sienna',
                  'brown', 'tan', 'burlywood', 'bisque'],
        'orange': ['moccasin', 'papayawhip', 'blanchedalmond', 'navajowhite', 'darkorange'],
        'yellow': ['lightgoldenrodyellow', 'lightyellow', 'beige', 'ivory', 'darkkhaki',
                   'palegoldenrod', 'khaki', 'lemonchiffon', 'gold', 'cornsilk', 'goldenrod',
                   'darkgoldenrod', 'floralwhite', 'oldlace'],
        'green': ['aquamarine', 'mediumaquamarine', 'mediumspringgreen', 'mintcream', 'springgreen',
                  'mediumseagreen', 'lime', 'green', 'darkgreen', 'limegreen', 'forestgreen',
                  'lightgreen', 'palegreen', 'darkseagreen', 'honeydew', 'lawngreen', 'chartreuse',
                  'greenyellow', 'darkolivegreen', 'yellowgreen', 'olivedrab'],
        'blue': ['blue', 'mediumblue', 'darkblue', 'navy', 'midnightblue', 'lavender', 'ghostwhite',
                 'royalblue', 'cornflowerblue', 'lightsteelblue', 'slategrey', 'slategray',
                 'lightslategray', 'lightslategrey', 'dodgerblue', 'aliceblue', 'steelblue',
                 'lightskyblue', 'skyblue', 'deepskyblue', 'lightblue', 'powderblue', 'cadetblue',
                 'darkturquoise', 'cyan', 'aqua', 'darkcyan', 'teal', 'darkslategray',
                 'darkslategrey', 'paleturquoise', 'lightcyan', 'azure', 'mediumturquoise',
                 'lightseagreen', 'turquoise'],
        'violet': ['darkmagenta', 'purple', 'violet', 'plum', 'thistle', 'mediumorchid',
                   'darkviolet', 'darkorchid', 'indigo', 'blueviolet', 'rebeccapurple',
                   'mediumpurple', 'mediumslateblue', 'darkslateblue', 'slateblue'],
        'pink': ['fuchsia', 'magenta', 'orchid', 'mediumvioletred', 'deeppink', 'hotpink',
                 'lavenderblush', 'palevioletred', 'crimson', 'pink', 'lightpink'],
        'black': ['black']
    }

    cname = cname.lower()

    for parent_color, color_list in distinctive_colors.items():
        if cname in color_list:
            return parent_color

    return "Undefined"
