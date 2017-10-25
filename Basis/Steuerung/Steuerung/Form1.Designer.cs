namespace Steuerung
{
    partial class Form1
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.PB_Bildschirm = new System.Windows.Forms.PictureBox();
            ((System.ComponentModel.ISupportInitialize)(this.PB_Bildschirm)).BeginInit();
            this.SuspendLayout();
            // 
            // PB_Bildschirm
            // 
            this.PB_Bildschirm.BackColor = System.Drawing.SystemColors.ActiveCaption;
            this.PB_Bildschirm.Location = new System.Drawing.Point(12, 12);
            this.PB_Bildschirm.Name = "PB_Bildschirm";
            this.PB_Bildschirm.Size = new System.Drawing.Size(656, 499);
            this.PB_Bildschirm.TabIndex = 0;
            this.PB_Bildschirm.TabStop = false;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(912, 621);
            this.Controls.Add(this.PB_Bildschirm);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Form1";
            this.Text = "Steuerung";
            ((System.ComponentModel.ISupportInitialize)(this.PB_Bildschirm)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.PictureBox PB_Bildschirm;
    }
}

