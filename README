Nifty Warp is a server-side plugin (built on bukkit) that allows for private, listed, unlisted warps.


WARP TYPES:
---------------------------------
"private"
- This is the default type of a warp if no type parameter is used when adding a warp.  When listing warps,
  private warps will only show up in their owner's lists.  When warping, only the owner will be able to warp to
  that named warp.

"unlisted"
- When listing warps, unlisted warps will only show up the their owner's lists.  However, when using warps, anyone
  can use the warp.  If the warp has the same name as one the owner has, you can use it by typing the fully qualified 
  name of the warp.  This warp type is useful if you want create useful warps to places that you can let your friends 
  know about, but that aren't important enough to clutter up everyone's list with.

"listed"
- This type of warp is able to be seen and used by anyone.


COMMANDS AND ALIASES
---------------------------------
In order to be able to function alongside other warp plugins, all of the main commands are unique to NiftyWarp and prefixed
with "nw". However, for ease of use, I've also included quite a few common and intuitive aliases. Here are the commands
listed using the aliases people are most likely to use. Below them are the main command and other alternates if you need to
use this in conjunction with another addon.

/addwarp <warpName>
Add a warp
- Command:    /nwadd
- Alternates: /nwa, /addwarp, /createwarp, /setwarp

/deletewarp <warpName>
Delete a warp
- Command:    /nwdelete
- Alternates: /nwd, /deletewarp, /removewarp

/sethome
Set your home warp (currently just makes a warp named "home")
- Command:    /nwhomeset
- Alternates: /nwhs, /homeset, /sethome

/home
Warp to your home (currently just warps to your "home" warp)
- Command:    /nwhome
- Alternates: /nwh, /home

/listwarps <worldName> <private | unlisted | listed>
Get the list of warps
- Command:    /nwlist
- Alternates: /nwl, /listwarps

/renamewarp <warpName> <newWarpName>
Rename an existing warp
- Command:    /nwrename
- Alternates: /nwr, /renamewarp

/setwarptype <warpName> private | unlisted | listed
Set the privacy/visibility of a warp after creation
- Command:    /nwset
- Alternates: /nws, /settype, /setwarptype

/warp <warpName>
Use a warp
- Command:    /nwwarp
- Alternates: /nw, /nww, /warp

/warptocoord x y z <worldName>
Warp to a specific coordinate
- Command:    /nwwarptocoord
- Alternates: /nwwtc, warptocoord, wtc, w2c



WARPING:
---------------------------------
All warps have an owner.  When attempting to use a warp that belongs to someone else, you may have to use the fully 
qualified name which is <owner>.<warpname> ... this situation only occurs when a warp that someone else owns has the 
same name as one that you own.  For listed warps, this will be obvious due to how it is listed.  However when 
attempting to use unlisted warps, you may have to take this into account on your own.  For instance, if I want to use 
an unlisted warp called "foo" that is owned by niftymonkey, I would type:

    /nwwarp niftymonkey.foo



CONFIGURATION:
---------------------------------
Currently there are only a few configuration options.  They are stored in the <pluginsDir>/NiftyWarp/NiftyWarp.yml file

messages:

    # this specifies whether or not to prefix all the NiftWarp-related messages with: [NiftyWarp] -
    # values: true | false  (defaults to true)
    show-prefix: true

    # this specifies whether or not to show the user a message when they don't have the proper permission set
    # this message also indicates the exact permission that needs to be set in order to use that command
    # this is intended to be a helper message for determining which permission is needed to use that command
    # values: true | false  (defaults to true)
    permissions:
        show-fail-message: true


warps:

    # this specifies the type to use when creating new warps without specifying the type
    # values: private | unlisted | listed  (defaults to unlisted)
    default-type: unlisted

    # this specifies the maximum number of warps to allow a user to create
    # value: a number  (defaults to 20)
    max-warps: 20


permissions:

    # this specifies whether or not to attempt to use the permissions plugin.  If set to false, the ruleset property
    # below will be used to determine permissions
    # values:  true | false  (defaults to true)
    use-plugin: true

    # this specifies what non-Permissions-plugin type ruleset you want to use.  This value is only used if use-plugin
    # is set to false.  ops-only means only Ops can use this plugin's commands.  ops-for-admin means only Ops can use 
    # the admin type functionality (such as renaming other people's warps).  ffa means Free For All ... meaning anyone 
    # can use any commands/functionality
    # values: ops-only | ops-for-admin | ffa (defaults to ffa)
    ruleset: ffa